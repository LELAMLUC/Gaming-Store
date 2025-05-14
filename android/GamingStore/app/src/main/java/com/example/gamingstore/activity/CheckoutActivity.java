package com.example.gamingstore.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private ImageView btnMoMoPayment, btnCODPayment, imgRadio1, imgRadio2, btnStandard, btnExpress, imgTruck1, imgTruck2;
    private TextView tvTimeToGo, tvTimeToGo2;
    private RecyclerView rvCheckout;
    private CheckoutAdapter checkoutAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        // Khởi tạo các ImageView (hoặc Button)
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

        // Thiết lập sự kiện cho nút MoMo Payment
        btnMoMoPayment.setOnClickListener(v -> {
            btnMoMoPayment.setBackgroundResource(R.drawable.bg_selected);
            imgRadio1.setImageResource(R.drawable.icon_button_radio_blue);
            btnCODPayment.setBackgroundResource(R.drawable.bg_normal);
            imgRadio2.setImageResource(R.drawable.icon_button_radio);
        });

        // Thiết lập sự kiện cho nút COD Payment
        btnCODPayment.setOnClickListener(v -> {
            btnCODPayment.setBackgroundResource(R.drawable.bg_selected);
            imgRadio2.setImageResource(R.drawable.icon_button_radio_blue);
            btnMoMoPayment.setBackgroundResource(R.drawable.bg_normal);
            imgRadio1.setImageResource(R.drawable.icon_button_radio);
        });

        btnStandard.setOnClickListener(v -> {
            btnStandard.setBackgroundResource(R.drawable.bg_selected);
            imgTruck1.setImageResource(R.drawable.icon_deli);
            tvTimeToGo.setTextColor(ContextCompat.getColor(this, R.color.blue_selected));
            btnExpress.setBackgroundResource(R.drawable.bg_normal);
            imgTruck2.setImageResource(R.drawable.icon_deli2);
            tvTimeToGo2.setTextColor(Color.parseColor("#CCCCCC"));
        });

        btnExpress.setOnClickListener(v -> {
            btnExpress.setBackgroundResource(R.drawable.bg_selected);
            imgTruck2.setImageResource(R.drawable.icon_deli);
            tvTimeToGo2.setTextColor(ContextCompat.getColor(this, R.color.blue_selected));
            btnStandard.setBackgroundResource(R.drawable.bg_normal);
            imgTruck1.setImageResource(R.drawable.icon_deli2);
            tvTimeToGo.setTextColor(Color.parseColor("#CCCCCC"));
        });

        // Khởi tạo RecyclerView
        rvCheckout = findViewById(R.id.rvCheckout);
        rvCheckout.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        checkoutAdapter = new CheckoutAdapter(cartItemList);
        rvCheckout.setAdapter(checkoutAdapter);

        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);

        // Kiểm tra accountId hợp lệ và gọi API
        if (accountId != -1) {
            getCartItemsFromApi(accountId);  // Gọi API với accountId
        }
    }

    // Di chuyển phương thức này ra ngoài onCreate
    private void getCartItemsFromApi(long accountId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCartItems(accountId).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemList = response.body();
                    checkoutAdapter = new CheckoutAdapter(cartItemList);
                    rvCheckout.setAdapter(checkoutAdapter);
                } else {
                    // Hiển thị thông báo lỗi khi không nhận được dữ liệu
                    // Hoặc xử lý các lỗi khác nếu cần
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc lỗi khác
            }
        });
    }
}
