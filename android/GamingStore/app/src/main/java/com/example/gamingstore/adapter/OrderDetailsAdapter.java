package com.example.gamingstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.activity.OrderDetailsActivity;
import com.example.gamingstore.model.Order;
import com.example.gamingstore.model.OrderItem;

import java.util.List;
public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private List<OrderItem> orderItems;

    public OrderDetailsAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_product, parent, false);
        return new OrderDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvQuantity.setText("Qty: " + item.getQuantity());
        double totalPrice = item.getProductPrice() * item.getQuantity();
        holder.tvProductPriceTotal.setText("$" + String.format("%.2f", totalPrice));

        Glide.with(holder.itemView.getContext())
                .load(item.getProductImage())
                .placeholder(R.drawable.icon_error)
                .error(R.drawable.icon_error)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    static class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvProductPriceTotal;
        ImageView imgProduct;

        public OrderDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvProductPriceTotal = itemView.findViewById(R.id.tvProductPriceTotal);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
