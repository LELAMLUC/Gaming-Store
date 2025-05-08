package com.example.gamingstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<String> searchList;
    private OnSearchItemDeleteListener deleteListener;

    // Interface để callback về Fragment khi cần xóa
    public interface OnSearchItemDeleteListener {
        void onDelete(String searchQuery);
    }

    public SearchAdapter(List<String> searchList, OnSearchItemDeleteListener deleteListener) {
        this.searchList = searchList;
        this.deleteListener = deleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_recent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String searchQuery = searchList.get(position);
        holder.tvName.setText(searchQuery);

        holder.deleteIcon.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(searchQuery); // callback để xóa trong SharedPreferences
            }
            searchList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, searchList.size());
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView deleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}
