package com.example.gamingstore.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.PopularProductAdapter;
import com.example.gamingstore.adapter.ProductAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultFragment extends Fragment {

    private RecyclerView recyclerView;
    private PopularProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        // Lấy từ khóa tìm kiếm từ Bundle
        String searchQuery = getArguments() != null ? getArguments().getString("searchQuery") : "";
        Log.d("SearchResultFragment", "Search query received: " + searchQuery);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerPopular);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);  // 2 cột
        recyclerView.setLayoutManager(gridLayoutManager);

        // Gọi API hoặc thực hiện tìm kiếm với từ khóa này
        searchWithQuery(searchQuery);

        return view;
    }

    private void searchWithQuery(String query) {
        // Tạo instance của ApiService
        ApiService apiService = RetrofitClient.getApiService();

        // Gọi API tìm kiếm sản phẩm với từ khóa
        apiService.searchProducts(query).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Cập nhật danh sách sản phẩm nhận được từ API
                    productList.clear();
                    productList.addAll(response.body());
                    // Thiết lập adapter cho RecyclerView
                    adapter = new PopularProductAdapter(getContext(), productList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("SearchResultFragment", "Error code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("SearchResultFragment", "API call failed: " + t.getMessage());
            }
        });
    }
}
