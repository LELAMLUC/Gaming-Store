package com.example.gamingstore.util;

import android.content.Context;
import android.widget.Toast;

public class InputValidator {

    public static boolean validatePasswordFields(Context context, String password, String confirmPassword) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(context, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "Mật khẩu không khớp nhau", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validateEmailField(Context context, String email) {
        if (email.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public static boolean validatePassField(Context context, String pass) {
        if (pass.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 8) {
            Toast.makeText(context, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validateOTPField(Context context, String otp) {
        if (otp.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập OTP", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
