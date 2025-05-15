package com.example.gamingstore.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.OrderDetailsAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends BaseActivity {

    private TextView tvSubtotal, tvTotal, tvShippingPhone, tvShippingAddress, tvOrderDate, tvDeliMethod,tvShipping2,tvDiscount2;
    private RecyclerView rvOrderItems;
    private OrderDetailsAdapter orderDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);

        // Ánh xạ view
        tvSubtotal = findViewById(R.id.tvSubtotal2);
        tvTotal = findViewById(R.id.tvTotal3);
        tvShippingPhone = findViewById(R.id.tvPhoneName);
        tvShippingAddress = findViewById(R.id.tvFullAddress2);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        rvOrderItems = findViewById(R.id.rvOrderItem2);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        tvDeliMethod = findViewById(R.id.tvDeliMethod);
        tvShipping2 = findViewById(R.id.tvShipping2);
        tvDiscount2 = findViewById(R.id.tvDiscount2);
        // Lấy orderId từ Intent
        long orderId = getIntent().getLongExtra("orderId", -1);
        if (orderId != -1) {
            loadOrderDetails(orderId);
        } else {
            Log.e("OrderDetailsActivity", "Không tìm thấy orderId trong Intent");
        }
    }

    private void loadOrderDetails(long orderId) {
        Log.d("OrderDetailsActivity", "Bắt đầu gọi API lấy chi tiết đơn hàng, orderId = " + orderId);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.d("OrderDetailsActivity", "API Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("OrderDetailsActivity", "API Response body: " + response.body().toString());
                    displayOrder(response.body());
                } else {
                    Log.e("OrderDetailsActivity", "Lỗi lấy chi tiết đơn hàng: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("OrderDetailsActivity", "Gọi API thất bại", t);
            }
        });
    }

    private void displayOrder(Order order) {
        Log.d("OrderDetailsActivity", "Hiển thị đơn hàng: subtotal=" + order.getSubtotal() +
                ", total=" + order.getTotal() +
                ", shippingPhone=" + order.getShippingPhone() +
                ", shippingAddress=" + order.getShippingAddress() +
                ", orderDate=" + order.getOrderDate() +
                ", số lượng items=" + (order.getItems() == null ? "null" : order.getItems().size()));

        tvSubtotal.setText(String.format("$%.2f", order.getSubtotal()));
        tvTotal.setText(String.format("$%.2f", order.getTotal()));
        tvDiscount2.setText(String.format("-$%.2f", order.getDiscount()));
        tvShippingPhone.setText(order.getShippingPhone());
        tvShippingAddress.setText(order.getShippingAddress());
        tvOrderDate.setText(order.getOrderDate());
        tvShipping2.setText(String.format("$%.2f", order.getShippingFee()));
        tvDeliMethod.setText(order.getDeliveryMethod()+ " Delivery");
        List<com.example.gamingstore.model.OrderItem> items = order.getItems();
        if (items != null && !items.isEmpty()) {
            orderDetailsAdapter = new OrderDetailsAdapter(items);

            rvOrderItems.setAdapter(orderDetailsAdapter);
        } else {
            Log.w("OrderDetailsActivity", "Danh sách items trong đơn hàng trống hoặc null");
        }
    }
}
