package com.example.gamingstore.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến RegisterActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    //    Nút Đăng Ký Tài Khoản
    public void btnRegister(View view) {
        EditText edtName = findViewById(R.id.edtNameRegister);
        String name = edtName.getText().toString().trim();

        // Lấy email, OTP, và mật khẩu từ EditText
        EditText edtEmail = findViewById(R.id.edtEmailRegister);
        String email = edtEmail.getText().toString().trim();

        EditText edtOTP = findViewById(R.id.edtOTP);
        String enteredOTP = edtOTP.getText().toString().trim();

        EditText edtPass = findViewById(R.id.edtPassRegister);
        String password = edtPass.getText().toString().trim();

        EditText edtPass2 = findViewById(R.id.edtPassRegister2);
        String password2 = edtPass2.getText().toString().trim();

        // Kiểm tra các trường thông tin
        if (!InputValidator.validateEmailField(this, email) ||
                !InputValidator.validateOTPField(this, enteredOTP) ||
                !InputValidator.validatePasswordFields(this, password, password2)) {
            return;
        }

        // Gọi API để check OTP
        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.verifyOTP(email, enteredOTP);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean isValid = response.body();
                    if (isValid) {
                        // Gọi API để tạo tài khoản, thêm name vào tham số
                        Call<Boolean> createAccountCall = apiService.createOrChangePassAccount(name, email, password);
                        createAccountCall.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful() && response.body() != null && response.body()) {
                                    Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Lỗi khi tạo tài khoản", Toast.LENGTH_SHORT).show();
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

    // Nút Lấy OTP
    public void btnTakeOTP(View view) {
        // Lấy email từ EditText
        EditText edtEmail = findViewById(R.id.edtEmailRegister);
        String email = edtEmail.getText().toString().trim();

        // Kiểm tra email hợp lệ
        CheckEmail checkEmail = new CheckEmail();
        if (!checkEmail.isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi api kiểm tra tồn tại mail trong data
        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.checkEmailExist(email);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean emailExists = response.body();
                    if (emailExists) {
                        Toast.makeText(RegisterActivity.this, "Email đã tồn tại trên hệ thống", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email chưa tồn tại, tạo OTP và gửi OTP tới mail người nhận
                        Call<Boolean> otpCall = apiService.generateOTP(email);
                        otpCall.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    boolean otpSent = response.body();
                                    if (otpSent) {
                                        Toast.makeText(getApplicationContext(), "OTP đã được gửi tới email của bạn!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Lỗi khi gửi OTP", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Lỗi khi tạo OTP", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("API Error", t.getMessage());
                                Toast.makeText(RegisterActivity.this, "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // Xử lý khi gọi API không thành công
                    Toast.makeText(RegisterActivity.this, "Lỗi khi kiểm tra email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Xử lý lỗi kết nối
                Log.e("API Error", t.getMessage());
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}