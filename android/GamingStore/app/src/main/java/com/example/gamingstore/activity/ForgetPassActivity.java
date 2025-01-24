package com.example.gamingstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamingstore.R;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.util.CheckEmail;
import com.example.gamingstore.util.InputValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void btnForgetPass(View view) {
        EditText edtEmail = findViewById(R.id.edtEmailForget);
        String email = edtEmail.getText().toString().trim();

        EditText edtOTP = findViewById(R.id.edtOTPForget);
        String enteredOTP = edtOTP.getText().toString().trim();

        EditText edtPass = findViewById(R.id.edtPassForget);
        String password = edtPass.getText().toString().trim();

        EditText edtPass2 = findViewById(R.id.edtPassForget2);
        String password2 = edtPass2.getText().toString().trim();

        // Kiểm tra các trường thông tin
        if (!InputValidator.validateEmailField(this, email) ||
                !InputValidator.validateOTPField(this, enteredOTP) ||
                !InputValidator.validatePasswordFields(this, password, password2)) {
            return; // Dừng lại nếu bất kỳ kiểm tra nào không đạt
        }
        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.verifyOTP(email, enteredOTP);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean isValid = response.body();
                    if (isValid) {
                        // Gọi API để tạo tài khoản
                        Call<Boolean> createAccountCall = apiService.createOrChangePassAccount(email, password);
                        createAccountCall.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful() && response.body() != null && response.body()) {
                                    Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Lỗi khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("API Error", t.getMessage());
                                Toast.makeText(getApplicationContext(), "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "OTP không hợp lệ hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi xác thực OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void btnTakeOTPForget(View view) {
        EditText edtEmail = findViewById(R.id.edtEmailForget);
        String email = edtEmail.getText().toString().trim();

        // Kiểm tra email hợp lệ
        CheckEmail checkEmail = new CheckEmail();
        if (!checkEmail.isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        //      gọi api kiểm tra tồn tại mail trong data
        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.checkEmailExist(email);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean emailExists = response.body();
                    if (!emailExists) {
                        Toast.makeText(ForgetPassActivity.this, "Email chưa tồn tại trên hệ thống", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email Đã tồn tại, tạo OTP và gửi OTP tới mail người nhận
                        Call<Boolean> otpCall = apiService.generateOTP(email);
                        otpCall.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    boolean otpSent = response.body();
                                    if (otpSent) {
                                        Toast.makeText(getApplicationContext(), "OTP đã được gửi tới email của bạn!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ForgetPassActivity.this, "Lỗi khi gửi OTP", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ForgetPassActivity.this, "Lỗi khi tạo OTP", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("API Error", t.getMessage());
                                Toast.makeText(ForgetPassActivity.this, "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // Xử lý khi gọi API không thành công
                    Toast.makeText(ForgetPassActivity.this, "Lỗi khi kiểm tra email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Xử lý lỗi kết nối
                Log.e("API Error", t.getMessage());
                Toast.makeText(ForgetPassActivity.this, "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
