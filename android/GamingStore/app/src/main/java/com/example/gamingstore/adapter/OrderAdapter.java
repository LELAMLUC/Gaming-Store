package com.example.gamingstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.activity.OrderDetailsActivity;
import com.example.gamingstore.model.Order;
import com.example.gamingstore.model.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set các thông tin đơn hàng nếu có (ví dụ: tổng tiền)
        holder.tvTotalPrice.setText("$" + String.format("%.2f", order.getTotal()));

        // Set adapter con cho RecyclerView sản phẩm trong đơn hàng
        List<OrderItem> items = order.getItems();
        OrderItemAdapter itemAdapter = new OrderItemAdapter(items);
        holder.recyclerViewItems.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewItems.setAdapter(itemAdapter);
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderId", order.getId());  // Truyền orderId đúng
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewItems;
        TextView tvTotalPrice;
        ImageButton btnDetails;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewItems = itemView.findViewById(R.id.rvWishlist); // id trong item_order.xml
            tvTotalPrice = itemView.findViewById(R.id.tvProductPriceTotal); // id tổng tiền trong item_order.xml
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
