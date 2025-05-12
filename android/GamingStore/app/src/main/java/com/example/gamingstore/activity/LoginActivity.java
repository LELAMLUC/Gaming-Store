package com.example.gamingstore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.gamingstore.util.InputValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up buttons
        TextView registerButton = findViewById(R.id.btnRegister);
        TextView forgotPassButton = findViewById(R.id.btnForgotPass);

        registerButton.setOnClickListener(v -> {
            // Chuyển đến RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPassButton.setOnClickListener(v -> {
            // Chuyển đến ForgetPassActivity
            Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
            startActivity(intent);
        });
    }

    public void btnLogin(View view) {
        EditText edtEmail = findViewById(R.id.edtEmail);
        String email = edtEmail.getText().toString().trim();

        EditText edtPass = findViewById(R.id.edtPass);
        String password = edtPass.getText().toString().trim();

        if (!InputValidator.validateEmailField(this, email) || !InputValidator.validatePassField(this, password)) {
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<Long> call = apiService.login(email, password);  // Chỉnh sửa để trả về Long (accountId)

        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Long accountId = response.body();
                    if (accountId != null) {
                        // Đăng nhập thành công, lưu accountId vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("accountId", accountId); // Lưu accountId vào SharedPreferences
                        editor.apply();

                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi xác thực tài khoản", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
