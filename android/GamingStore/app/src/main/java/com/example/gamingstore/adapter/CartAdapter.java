package com.example.gamingstore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private OnCartChangeListener cartChangeListener;

    // Giao diện callback để Fragment cập nhật lại tổng tiền
    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getProduct().getName());
        holder.tvPrice.setText(String.format("$%.2f", item.getProduct().getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        double totalPrice = item.getProduct().getPrice() * item.getQuantity();
        holder.tvPriceTotal.setText(String.format("$%.2f", totalPrice));

        Glide.with(context)
                .load(item.getProduct().getImageUrl())
                .into(holder.imgProduct);

        try {
            String colorCode = item.getColor();
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(Color.parseColor(colorCode));
            shape.setStroke(5, Color.parseColor("#00F0FF"));
            holder.imgColor.setBackground(shape);
        } catch (IllegalArgumentException e) {
            Log.e("ColorParse", "Lỗi parse màu: " + item.getColor());
        }

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            if (cartChangeListener != null) cartChangeListener.onCartChanged(); // Cập nhật lại tổng
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                if (cartChangeListener != null) cartChangeListener.onCartChanged(); // Cập nhật lại tổng
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvPriceTotal;
        ImageView imgProduct, btnPlus, btnMinus;
        LinearLayout imgColor;

        public CartViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantityNumber);
            tvPriceTotal = itemView.findViewById(R.id.tvPriceTotal);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            imgColor = itemView.findViewById(R.id.imgColor);
        }
    }
}
