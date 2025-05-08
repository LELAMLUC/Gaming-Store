package com.example.gamingstore.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.gamingstore.model.Category;
import com.example.gamingstore.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

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
}