// BaseActivity.java
package com.example.gamingstore.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sau khi Activity con gọi setContentView, layout đã inflate xong
        View rootView = findViewById(android.R.id.content);
        if (rootView instanceof ViewGroup) {
            setupBackButton((ViewGroup) rootView);
        }
    }

    private void setupBackButton(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);

            if ("back_button".equals(child.getTag())) {
                child.setOnClickListener(v -> {
                    // Xử lý quay về trang trước
                    getOnBackPressedDispatcher().onBackPressed();
                });
                return; // tìm thấy rồi dừng tìm
            }

            if (child instanceof ViewGroup) {
                setupBackButton((ViewGroup) child);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    hideKeyboard(v);
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
