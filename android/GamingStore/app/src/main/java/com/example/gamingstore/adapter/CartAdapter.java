package com.example.gamingstore.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private OnCartChangeListener cartChangeListener;

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
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                if (cartChangeListener != null) cartChangeListener.onCartChanged();
            }
        });

        // Xử lý nút xóa (Trash)
        holder.btnTrash.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            long accountId = sharedPreferences.getLong("accountId", -1);
            CartItem itemToRemove = cartItems.get(adapterPosition);

            long productId = itemToRemove.getProduct().getId();
            String color = itemToRemove.getColor();

            ApiService apiService = RetrofitClient.getApiService();
            apiService.removeFromCart(accountId, productId, color)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                                cartItems.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                notifyItemRangeChanged(adapterPosition, cartItems.size());
                                if (cartChangeListener != null) cartChangeListener.onCartChanged();
                                Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Log.e("API_ERROR", "Kết nối lỗi: " + t.getMessage(), t);
                            Toast.makeText(context, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        });

    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvPriceTotal;
        ImageView imgProduct, btnPlus, btnMinus, btnTrash;
        LinearLayout imgColor;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvQuantity);
            tvQuantity = itemView.findViewById(R.id.tvQuantityNumber);
            tvPriceTotal = itemView.findViewById(R.id.tvProductPriceTotal);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            imgColor = itemView.findViewById(R.id.imgColor);
            btnTrash = itemView.findViewById(R.id.btnTrash);
        }
    }

    private long getCurrentAccountId() {
        // TODO: Thay thế bằng cách lấy accountId của user hiện tại (SharedPreferences, hoặc truyền từ Activity/Fragment)
        return 1L;
    }
}
