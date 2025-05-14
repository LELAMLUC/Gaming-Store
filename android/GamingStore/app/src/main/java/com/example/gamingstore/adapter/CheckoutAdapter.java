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
import com.example.gamingstore.model.CartItem;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private List<CartItem> cartItemList;

    public CheckoutAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        CartItem.Product product = item.getProduct();

        // Cập nhật tên sản phẩm
        if (holder.tvName != null) {
            holder.tvName.setText(product.getName());
        }

        // Tính toán giá tổng cho sản phẩm
        double totalPrice = product.getPrice() * item.getQuantity();
        if (holder.tvPriceTotal != null) {
            holder.tvPriceTotal.setText(String.format("$%.2f", totalPrice));
        }

        // Cập nhật số lượng sản phẩm
        if (holder.tvQuantity != null) {
            holder.tvQuantity.setText("Qty: " + item.getQuantity());
        }

        // Tải ảnh sản phẩm bằng Glide
        if (holder.imgProduct != null) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.icon_error)
                    .into(holder.imgProduct);
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPriceTotal, tvQuantity;
        ImageView imgProduct;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            // Lấy các view trong item_checkout.xml
            tvName = itemView.findViewById(R.id.tvName);
            tvPriceTotal = itemView.findViewById(R.id.tvPriceTotal);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
