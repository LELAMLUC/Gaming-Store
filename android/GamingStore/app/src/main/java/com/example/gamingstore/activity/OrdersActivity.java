package com.example.gamingstore.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.OrderAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends BaseActivity {

    RecyclerView recyclerOrders;
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerOrders = findViewById(R.id.rvOrderItems);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        // Lấy accountId từ SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);

        if (accountId != -1) {
            loadOrders(accountId);
        } else {
            Log.e("OrdersActivity", "Không tìm thấy accountId trong SharedPreferences");
        }
    }

    private void loadOrders(long accountId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getOrdersByAccountId(accountId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body();
                    orderAdapter = new OrderAdapter(OrdersActivity.this, orders);
                    recyclerOrders.setAdapter(orderAdapter);
                } else {
                    Log.e("OrdersActivity", "Lỗi lấy đơn hàng: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("OrdersActivity", "Gọi API thất bại", t);
            }
        });
    }
}
