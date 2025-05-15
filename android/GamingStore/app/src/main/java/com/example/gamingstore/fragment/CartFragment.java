package com.example.gamingstore.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.activity.CheckoutActivity;
import com.example.gamingstore.adapter.CartAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.CartItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private ApiService apiService;
    private ImageButton btnCheckout;
    private TextView tvSubTotal, tvShipping, tvTotal;

    public CartFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.itemCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(getContext(), cartItems);
        recyclerView.setAdapter(cartAdapter);

        // Gán TextView
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvShipping = view.findViewById(R.id.tvShipping);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        // Gán callback cập nhật tổng
        cartAdapter.setOnCartChangeListener(() -> updateCartSummary());

        apiService = RetrofitClient.getApiService();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);

        if (accountId != -1) {
            loadCartItems(accountId);
        } else {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }
        btnCheckout.setOnClickListener(v -> {
            // Chuyển qua Activity Checkout
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void loadCartItems(long accountId) {
        Call<List<CartItem>> call = apiService.getCartItems(accountId);
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    cartItems.addAll(response.body());
                    cartAdapter.notifyDataSetChanged();
                    updateCartSummary();
                } else {
                    Toast.makeText(getContext(), "Không lấy được giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CartFragment", "Error loading cart", t);
            }
        });
    }

    // ✅ Hàm cập nhật tổng tiền, gọi lại mỗi lần thay đổi số lượng
    private void updateCartSummary() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getProduct().getPrice() * item.getQuantity();
        }

        double shipping = 9.99;
        double total = subtotal + shipping;

        tvSubTotal.setText(String.format("$%.2f", subtotal));
        tvShipping.setText(String.format("$%.2f", shipping));
        tvTotal.setText(String.format("$%.2f", total));
    }
}
