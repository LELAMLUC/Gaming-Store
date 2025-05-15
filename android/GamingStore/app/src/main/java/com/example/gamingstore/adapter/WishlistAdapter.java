package com.example.gamingstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.model.Product;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public WishlistAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getName());

        // Format giá với 2 chữ số thập phân
        String priceText = String.format("$%.2f", product.getPrice());
        holder.tvQuantity.setText(priceText);

        holder.tvRating.setText(String.valueOf(product.getRating()));

        // Load ảnh bằng Glide
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.icon_error) // hình placeholder nếu muốn
                .into(holder.imgProduct);

        // Click item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvDiscount, tvProductName, tvQuantity, tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }

    // Thêm hàm cập nhật danh sách mới nếu cần
    public void updateList(List<Product> newList) {
        productList = newList;
        notifyDataSetChanged();
    }
}
