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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment {

    private EditText edtSearch;

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

        return view;
    }

    // Hàm xử lý tìm kiếm
    public void onSearch(String searchQuery) {
        // Thực hiện tìm kiếm và lưu từ khóa vào danh sách recent search
        saveSearch(searchQuery);
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

        // Cập nhật dữ liệu cho RecyclerView
        SearchAdapter adapter = new SearchAdapter(searchList, query -> deleteSearch(query));

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

}
