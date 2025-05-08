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
import com.example.gamingstore.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private int selectedPosition = -1;

    // Interface callback
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private OnCategoryClickListener listener;

    // Setter để gán listener
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        Glide.with(context)
                .load(category.getIcon())
                .placeholder(R.drawable.icon_error)
                .error(R.drawable.icon_error)
                .into(holder.categoryIcon);

        // Thay đổi background khi được chọn
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_selected);
            holder.categoryName.setTextColor(context.getResources().getColor(R.color.blue_selected));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_normal);
            holder.categoryName.setTextColor(context.getResources().getColor(R.color.blue_selected));
        }

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            // Gọi callback nếu có
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.imgCategoryIcon);
            categoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
