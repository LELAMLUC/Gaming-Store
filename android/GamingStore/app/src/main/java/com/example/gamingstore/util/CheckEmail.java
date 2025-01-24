package com.example.gamingstore.util;
import android.util.Patterns;

import com.example.gamingstore.activity.RegisterActivity;


public class CheckEmail {
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
