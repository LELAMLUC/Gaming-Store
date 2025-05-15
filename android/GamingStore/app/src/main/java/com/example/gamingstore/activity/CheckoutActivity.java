package com.example.gamingstore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.CheckoutAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.CartItem;
import com.example.gamingstore.model.Voucher;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends BaseActivity {

    private ImageView btnMoMoPayment, btnCODPayment, imgRadio1, imgRadio2, btnStandard, btnExpress, imgTruck1, imgTruck2;
    private TextView tvTimeToGo, tvTimeToGo2, tvShip, tvSubtotal, tvTotal2, tvDiscount1;
    private EditText edtVoucher;
    private ImageButton btnApplyCode, btnPlaceOrder;  // phải là ImageButton
    private RecyclerView rvCheckout;
    private CheckoutAdapter checkoutAdapter;
    private List<CartItem> cartItemList;
    private String deliveryMethod = "";
    private double subtotal = 0.0;
    private double shippingFee = 0.0;
    private double discountAmount = 0.0;

    private final Locale localeUS = Locale.US;
    private final NumberFormat currencyUS = NumberFormat.getCurrencyInstance(localeUS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        btnMoMoPayment = findViewById(R.id.btnMoMoPayment);
        btnCODPayment = findViewById(R.id.btnCODPayment);
        imgRadio1 = findViewById(R.id.imgRadio1);
        imgRadio2 = findViewById(R.id.imgRadio2);
        btnStandard = findViewById(R.id.btnStandard);
        btnExpress = findViewById(R.id.btnExpress);
        imgTruck1 = findViewById(R.id.imgTruck1);
        imgTruck2 = findViewById(R.id.imgTruck2);
        tvTimeToGo = findViewById(R.id.tvTimeToGo);
        tvTimeToGo2 = findViewById(R.id.tvTimeToGo2);
        btnApplyCode =findViewById(R.id.btnApplyCode);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal2 = findViewById(R.id.tvTotal2);
        tvShip = findViewById(R.id.tvShip);
        tvDiscount1 = findViewById(R.id.tvDiscount1);
        edtVoucher= findViewById(R.id.edtVoucher);
        btnPlaceOrder= findViewById(R.id.btnPlaceOrder);
        btnMoMoPayment.setOnClickListener(v -> {
            btnMoMoPayment.setBackgroundResource(R.drawable.bg_selected);
            imgRadio1.setImageResource(R.drawable.icon_button_radio_blue);
            btnCODPayment.setBackgroundResource(R.drawable.bg_normal);
            imgRadio2.setImageResource(R.drawable.icon_button_radio);
        });

        btnCODPayment.setOnClickListener(v -> {
            btnCODPayment.setBackgroundResource(R.drawable.bg_selected);
            imgRadio2.setImageResource(R.drawable.icon_button_radio_blue);
            btnMoMoPayment.setBackgroundResource(R.drawable.bg_normal);
            imgRadio1.setImageResource(R.drawable.icon_button_radio);
        });

        btnStandard.setOnClickListener(v -> {
            shippingFee = 9.99;
            tvShip.setText(currencyUS.format(shippingFee));
            deliveryMethod = "Standard";
            btnStandard.setBackgroundResource(R.drawable.bg_selected);
            imgTruck1.setImageResource(R.drawable.icon_deli);
            tvTimeToGo.setTextColor(ContextCompat.getColor(this, R.color.blue_selected));

            btnExpress.setBackgroundResource(R.drawable.bg_normal);
            imgTruck2.setImageResource(R.drawable.icon_deli2);
            tvTimeToGo2.setTextColor(Color.parseColor("#CCCCCC"));

            updateTotal();
        });

        btnExpress.setOnClickListener(v -> {
            shippingFee = 19.99;
            tvShip.setText(currencyUS.format(shippingFee));
            deliveryMethod = "Express";
            btnExpress.setBackgroundResource(R.drawable.bg_selected);
            imgTruck2.setImageResource(R.drawable.icon_deli);
            tvTimeToGo2.setTextColor(ContextCompat.getColor(this, R.color.blue_selected));

            btnStandard.setBackgroundResource(R.drawable.bg_normal);
            imgTruck1.setImageResource(R.drawable.icon_deli2);
            tvTimeToGo.setTextColor(Color.parseColor("#CCCCCC"));

            updateTotal();
        });

        rvCheckout = findViewById(R.id.rvCheckout);
        rvCheckout.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        checkoutAdapter = new CheckoutAdapter(cartItemList, totalPrice -> {
            subtotal = totalPrice;
            tvSubtotal.setText(currencyUS.format(subtotal));
            updateTotal();
        });
        rvCheckout.setAdapter(checkoutAdapter);

        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);

        if (accountId != -1) {
            getCartItemsFromApi(accountId);
        }
        btnApplyCode.setOnClickListener(v -> {
            String code = edtVoucher.getText().toString().trim();
            if (!code.isEmpty()) {
                applyVoucher(code);
            } else {
                Toast.makeText(this, "Please enter voucher code", Toast.LENGTH_SHORT).show();
            }
        });
        btnPlaceOrder.setOnClickListener(v -> {
            if (deliveryMethod.isEmpty()) {
                Toast.makeText(CheckoutActivity.this, "Please select a delivery method", Toast.LENGTH_SHORT).show();
                return;
            }
            double total = subtotal + shippingFee - discountAmount;
            if (accountId == -1) {
                Toast.makeText(CheckoutActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService();
            Call<Boolean> call = apiService.createOrder(
                    accountId,
                    subtotal,
                    shippingFee,
                    discountAmount,
                    total,
                    deliveryMethod
            );

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        Toast.makeText(CheckoutActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        // Delay 1.5 giây trước khi chuyển
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }, 500); // 1500ms = 1.5 giây
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(CheckoutActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void getCartItemsFromApi(long accountId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCartItems(accountId).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemList = response.body();

                    double tempSubtotal = 0;
                    for (CartItem item : cartItemList) {
                        tempSubtotal += item.getProduct().getPrice() * item.getQuantity();
                    }
                    subtotal = tempSubtotal;

                    tvSubtotal.setText(currencyUS.format(subtotal));

                    checkoutAdapter = new CheckoutAdapter(cartItemList, totalPrice -> {
                        subtotal = totalPrice;
                        tvSubtotal.setText(currencyUS.format(subtotal));
                        updateTotal();
                    });
                    rvCheckout.setAdapter(checkoutAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void updateTotal() {
        double total = subtotal + shippingFee - discountAmount;
        tvTotal2.setText(currencyUS.format(total));
    }
    private void applyVoucher(String code) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.applyVoucher(code).enqueue(new Callback<Voucher>() {
            @Override
            public void onResponse(Call<Voucher> call, Response<Voucher> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Voucher voucher = response.body();

                    discountAmount = subtotal * voucher.getDiscountPercentage() / 100.0;

                    tvDiscount1.setText(currencyUS.format(-discountAmount));
                    updateTotal();

                    Toast.makeText(CheckoutActivity.this, "Voucher applied: " + voucher.getCode(), Toast.LENGTH_SHORT).show();
                } else {
                    discountAmount = 0.0;
                    tvDiscount1.setText(currencyUS.format(0));
                    updateTotal();
                    Toast.makeText(CheckoutActivity.this, "Invalid voucher code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Voucher> call, Throwable t) {
                discountAmount = 0.0;
                tvDiscount1.setText(currencyUS.format(0));
                updateTotal();
                Toast.makeText(CheckoutActivity.this, "Failed to apply voucher", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
