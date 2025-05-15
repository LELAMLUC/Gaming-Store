package com.example.gamingstore.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HomeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerFeatured;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerPopular;
    private PopularProductAdapter adapter;
    private BottomNavigationView bottomNav;
    private ApiService apiService;
    private EditText edtSearch;

    private ImageView btnCart2, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        edtSearch = findViewById(R.id.edtEmail2);
        recyclerPopular = findViewById(R.id.rvWishlist);
        recyclerPopular.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerFeatured = findViewById(R.id.recycleFeatured);
        recyclerFeatured.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        bottomNav = findViewById(R.id.bottom_navigation);
        apiService = RetrofitClient.getApiService();

        btnCart2 = findViewById(R.id.btnCart2);
        btnProfile = findViewById(R.id.btnProfile);

        // Xử lý khi nhấn nút cart hoặc profile ở đầu màn hình
        btnCart2.setOnClickListener(v -> loadFragment(new CartFragment()));
        btnProfile.setOnClickListener(v -> loadFragment(new ProfileFragment()));

        // Xử lý tìm kiếm
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchQuery = edtSearch.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    Log.d("SearchFragment", "Search query: " + searchQuery);
                    onSearch(searchQuery);
                }
                return true;
            }
            return false;
        });

        // Gọi API để load dữ liệu
        loadCategories();
        loadAllProducts();
        loadPopularProducts();

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
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

        Bundle bundle = new Bundle();
        bundle.putString("searchQuery", searchQuery);

        SearchResultFragment searchResultFragment = new SearchResultFragment();
        searchResultFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, searchResultFragment);
        transaction.addToBackStack(null);

        new android.os.Handler().postDelayed(() -> {
            transaction.commit();
        }, 400);
    }

    private void saveSearch(String searchQuery) {
        SharedPreferences preferences = this.getSharedPreferences("RecentSearches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> recentSearches = preferences.getStringSet("recent_searches", new HashSet<>());
        Log.d("SearchFragment", "Current saved searches before adding new: " + recentSearches);

        recentSearches.add(searchQuery);

        if (recentSearches.size() > 4) {
            recentSearches.remove(recentSearches.iterator().next());
        }

        boolean success = editor.putStringSet("recent_searches", recentSearches).commit();
        Log.d("SearchFragment", "Save successful: " + success);
        Log.d("SearchFragment", "Updated saved searches after adding: " + recentSearches);
    }
}
