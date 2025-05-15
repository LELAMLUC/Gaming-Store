package com.example.gamingstore.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBackButton(view);
        setupHideKeyboardOnTouch(view);
    }

    private void setupBackButton(View root) {
        if (root instanceof ViewGroup) {
            findBackButtonAndSetup((ViewGroup) root);
        }
    }

    private void findBackButtonAndSetup(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);

            if ("back_button".equals(child.getTag())) {
                child.setOnClickListener(v -> {
                    // Quay về trang trước trong Fragment
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                });
                return;
            }

            if (child instanceof ViewGroup) {
                findBackButtonAndSetup((ViewGroup) child);
            }
        }
    }

    private void setupHideKeyboardOnTouch(View root) {
        root.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus instanceof EditText) {
                    Rect outRect = new Rect();
                    currentFocus.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        }
                        currentFocus.clearFocus();
                    }
                }
            }
            return false;
        });
    }
}
