package com.example.gamingstore.activity;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.CategoryAdapter;
import com.example.gamingstore.adapter.PopularProductAdapter;
import com.example.gamingstore.adapter.ProductAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.fragment.CartFragment;
import com.example.gamingstore.fragment.ProfileFragment;
import com.example.gamingstore.fragment.SearchFragment;
import com.example.gamingstore.fragment.SearchResultFragment;
import com.example.gamingstore.model.Category;
import com.example.gamingstore.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerFeatured;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerPopular;
    private PopularProductAdapter adapter;
    private BottomNavigationView bottomNav;
    private ApiService apiService;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        EditText edtSearch = findViewById(R.id.edtEmail2);  // EditText của bạn

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
        recyclerPopular = findViewById(R.id.recyclerPopular);
        recyclerPopular.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerFeatured = findViewById(R.id.recycleFeatured);
        recyclerFeatured.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        bottomNav = findViewById(R.id.bottom_navigation);
        apiService = RetrofitClient.getApiService();

        loadCategories();
        loadAllProducts();
        loadPopularProducts();

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Quay về màn hình chính
                getSupportFragmentManager().popBackStackImmediate(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                return true;
            } else if (id == R.id.nav_search) {
                loadFragment(new SearchFragment());
                return true;
            } else if (id == R.id.nav_cart) {
                loadFragment(new CartFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });


    }

    private void loadAllProducts() {
        apiService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productAdapter = new ProductAdapter(HomeActivity.this, response.body());
                    recyclerFeatured.setAdapter(productAdapter);
                } else {
                    Log.e("API", "Không lấy được danh sách sản phẩm. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API", "Lỗi khi gọi API: " + t.getMessage(), t);
            }
        });
    }

    private void loadPopularProducts() {
        apiService.getPopularProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new PopularProductAdapter(HomeActivity.this, response.body());
                    recyclerPopular.setAdapter(adapter);
                } else {
                    Log.e("API", "Không lấy được danh sách sản phẩm phổ biến. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API", "Lỗi khi gọi API: " + t.getMessage(), t);
            }
        });
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryAdapter = new CategoryAdapter(HomeActivity.this, response.body());
                    recyclerView.setAdapter(categoryAdapter);

                    categoryAdapter.setOnCategoryClickListener(category -> {
                        long categoryId = category.getId();
                        if (categoryId == 1) {
                            loadAllProducts();
                        } else {
                            apiService.getProductsByCategory(categoryId).enqueue(new Callback<List<Product>>() {
                                @Override
                                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        productAdapter = new ProductAdapter(HomeActivity.this, response.body());
                                        recyclerFeatured.setAdapter(productAdapter);
                                    } else {
                                        Log.e("API", "Không lấy được sản phẩm theo category");
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Product>> call, Throwable t) {
                                    Log.e("API", "Lỗi API khi lấy sản phẩm theo category: " + t.getMessage());
                                }
                            });
                        }
                    });
                } else {
                    Log.e("API", "Response không thành công");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
    public void onSearch(String searchQuery) {
        saveSearch(searchQuery);

        // Tạo Bundle để truyền dữ liệu tìm kiếm
        Bundle bundle = new Bundle();
        bundle.putString("searchQuery", searchQuery);  // Truyền từ khóa tìm kiếm

        // Chuyển sang SearchResultFragment và truyền dữ liệu
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        searchResultFragment.setArguments(bundle);  // Gửi Bundle vào Fragment

        // Tiến hành thay đổi fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); // Thay thế getParentFragmentManager() bằng getSupportFragmentManager()
        transaction.replace(R.id.fragmentContainer, searchResultFragment);

        transaction.addToBackStack(null);  // Cho phép quay lại fragment trước đó

        // Delay 200ms để cảm giác tự nhiên hơn
        new android.os.Handler().postDelayed(() -> {
            transaction.commit();
        }, 400);
    }

    private void saveSearch(String searchQuery) {
        SharedPreferences preferences = this.getSharedPreferences("RecentSearches", Context.MODE_PRIVATE);  // Thay getActivity() bằng this
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

}