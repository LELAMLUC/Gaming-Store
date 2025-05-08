package com.example.gamingstore.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lấy root view của Activity
        View rootView = findViewById(android.R.id.content);

        // Áp dụng listener toàn bộ activity
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Kiểm tra khi người dùng chạm vào bất kỳ đâu ngoài EditText
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View view = getCurrentFocus();
                    if (view instanceof EditText) {
                        Rect outRect = new Rect();
                        view.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            // Nếu người dùng chạm ra ngoài EditText, ẩn bàn phím
                            hideKeyboard(view);
                        }
                    }
                }
                return false;
            }
        });
    }

    // Hàm ẩn bàn phím
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
