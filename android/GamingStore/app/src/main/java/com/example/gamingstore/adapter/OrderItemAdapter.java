package com.example.gamingstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.model.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItems;

    public OrderItemAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_product, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvQuantity.setText("Qty: " + item.getQuantity());

        double totalPrice = item.getProductPrice() * item.getQuantity();
        holder.tvProductPriceTotal.setText("$" + String.format("%.2f", totalPrice));

        // Load ảnh từ URL bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(item.getProductImage())  // Cần có getter getProductImage() trong model OrderItem
                .placeholder(R.drawable.icon_error) // ảnh tạm khi đang load
                .error(R.drawable.icon_error)             // ảnh khi load lỗi
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvProductPriceTotal;
        ImageView imgProduct;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvProductPriceTotal = itemView.findViewById(R.id.tvProductPriceTotal);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
