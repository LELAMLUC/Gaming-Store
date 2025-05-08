package com.example.gamingstore.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.SearchRecentAdapter;
import com.example.gamingstore.adapter.SearchPopularAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText edtSearch;
    private ApiService apiService;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        edtSearch = view.findViewById(R.id.edtEmail3);  // EditText của bạn

        // Thiết lập sự kiện khi người dùng nhấn nút gửi (Enter) trên bàn phím
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            // Kiểm tra xem nút "gửi" (Enter) đã được nhấn chưa
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Lấy từ khóa tìm kiếm từ EditText
                String searchQuery = edtSearch.getText().toString().trim();

                // Kiểm tra nếu người dùng đã nhập từ khóa
                if (!searchQuery.isEmpty()) {
                    Log.d("SearchFragment", "Search query: " + searchQuery);  // Log để kiểm tra từ khóa tìm kiếm
                    onSearch(searchQuery);
                }
                return true; // Đánh dấu rằng sự kiện đã được xử lý
            }
            return false;
        });

        // Tải danh sách tìm kiếm gần đây
        loadRecentSearches(view);

        // Tải danh sách sản phẩm phổ biến
        loadPopularProducts(view);

        return view;
    }

    // Hàm xử lý tìm kiếm
    public void onSearch(String searchQuery) {
        saveSearch(searchQuery);

        // Tạo Bundle để truyền dữ liệu tìm kiếm
        Bundle bundle = new Bundle();
        bundle.putString("searchQuery", searchQuery);  // Truyền từ khóa tìm kiếm

        // Chuyển sang SearchResultFragment và truyền dữ liệu
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        searchResultFragment.setArguments(bundle);  // Gửi Bundle vào Fragment

        // Tiến hành thay đổi fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, searchResultFragment);
        transaction.addToBackStack(null);  // Cho phép quay lại fragment trước đó

        // Delay 200ms để cảm giác tự nhiên hơn
        new android.os.Handler().postDelayed(() -> {
            transaction.commit();
        }, 400);
    }


    private void saveSearch(String searchQuery) {
        SharedPreferences preferences = getActivity().getSharedPreferences("RecentSearches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Lấy danh sách tìm kiếm gần đây đã lưu
        Set<String> recentSearches = preferences.getStringSet("recent_searches", new HashSet<>());
        Log.d("SearchFragment", "Current saved searches before adding new: " + recentSearches);  // Log để kiểm tra danh sách cũ

        // Thêm tìm kiếm mới vào danh sách
        recentSearches.add(searchQuery);

        // Giới hạn danh sách chỉ giữ 4 tìm kiếm gần nhất
        if (recentSearches.size() > 4) {
            recentSearches.remove(recentSearches.iterator().next());  // Xóa tìm kiếm cũ nhất
        }

        // Lưu lại danh sách tìm kiếm
        boolean success = editor.putStringSet("recent_searches", recentSearches).commit(); // Sử dụng commit() thay vì apply()
        Log.d("SearchFragment", "Save successful: " + success);
        Log.d("SearchFragment", "Updated saved searches after adding: " + recentSearches);
    }

    private void loadRecentSearches(View view) {
        SharedPreferences preferences = getActivity().getSharedPreferences("RecentSearches", Context.MODE_PRIVATE);
        Set<String> recentSearches = preferences.getStringSet("recent_searches", new HashSet<>());

        // Chuyển Set thành danh sách (List) để hiển thị trong RecyclerView
        List<String> searchList = new ArrayList<>(recentSearches);

        // Log để kiểm tra danh sách tìm kiếm gần đây
        Log.d("SearchFragment", "Recent searches loaded: " + searchList);

        // Lấy RecyclerView từ view đã được tạo
        RecyclerView recyclerView = view.findViewById(R.id.SearchRecent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cập nhật dữ liệu cho RecyclerView với callback sao chép từ khóa vào EditText
        SearchRecentAdapter adapter = new SearchRecentAdapter(searchList, query -> {
            edtSearch.setText(query); // Sao chép từ khóa vào EditText
            onSearch(query);  // Gọi hàm xử lý tìm kiếm
        }, query -> deleteSearch(query));
        recyclerView.setAdapter(adapter);
    }


    private void deleteSearch(String searchQuery) {
        SharedPreferences preferences = getActivity().getSharedPreferences("RecentSearches", Context.MODE_PRIVATE);
        Set<String> recentSearches = preferences.getStringSet("recent_searches", new HashSet<>());
        if (recentSearches.contains(searchQuery)) {
            recentSearches.remove(searchQuery);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet("recent_searches", recentSearches);
            editor.apply();
            Log.d("SearchFragment", "Deleted search query: " + searchQuery);
        }
    }

    // Hàm tải sản phẩm phổ biến từ API
    private void loadPopularProducts(View view) {
        RecyclerView recyclerPopular = view.findViewById(R.id.recyclerPopular);  // Lấy RecyclerView mới cho sản phẩm phổ biến
        recyclerPopular.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy ApiService từ RetrofitClient
        ApiService apiService = RetrofitClient.getApiService();  // Sử dụng RetrofitClient để lấy ApiService
        apiService.getTopSellingProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();

                    SearchPopularAdapter adapter = new SearchPopularAdapter(products, product -> {
                        String query = product.getName();
                        edtSearch.setText(query);  // Ghi lại từ khóa vào ô tìm kiếm (tùy chọn)
                        onSearch(query);           // Gọi hàm xử lý tìm kiếm
                    });

                    recyclerPopular.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("SearchFragment", "Failed to load popular products", t);
            }
        });
    }
}
