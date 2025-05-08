package com.example.gamingstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.model.Product;

import java.util.List;
public class SearchPopularAdapter extends RecyclerView.Adapter<SearchPopularAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private List<Product> popularSearchList;
    private OnItemClickListener listener;

    public SearchPopularAdapter(List<Product> popularSearchList, OnItemClickListener listener) {
        this.popularSearchList = popularSearchList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_popular, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = popularSearchList.get(position);
        holder.tvPopular.setText(product.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularSearchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPopular;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPopular = itemView.findViewById(R.id.tvPopular);
        }
    }
}
