package com.example.gamingstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.activity.ProductDetailActivity;
import com.example.gamingstore.model.Product;

import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.PopularViewHolder> {

    private Context context;
    private List<Product> productList;

    public PopularProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("$" + product.getPrice());
        holder.tvRating.setText(String.valueOf(product.getRating()));

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.icon_error)
                .into(holder.imgProduct);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productName", product.getName());
            intent.putExtra("productDescription", product.getDescription());
            intent.putExtra("productId", product.getId()); // sửa lại key cho đúng
            context.startActivity(intent);
            Log.d("ProductAdapter", "Clicked product ID: " + product.getId());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvRating;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvQuantity);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}
