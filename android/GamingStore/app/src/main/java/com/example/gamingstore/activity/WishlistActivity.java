package com.example.gamingstore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamingstore.R;
import com.example.gamingstore.adapter.WishlistAdapter;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.Product;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends BaseActivity {

    private RecyclerView rvWishlist;
    private WishlistAdapter wishlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        rvWishlist = findViewById(R.id.rvWishlist);
        rvWishlist.setLayoutManager(new GridLayoutManager(this, 2));

        wishlistAdapter = new WishlistAdapter(this, new ArrayList<>(), product -> {
            // Xử lý khi bấm vào item wishlist
            Intent intent = new Intent(WishlistActivity.this, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId()); // Giả sử bạn truyền id sản phẩm
            startActivity(intent);
        });

        rvWishlist.setAdapter(wishlistAdapter);

        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);

        if (accountId != -1) {
            fetchWishlist(accountId);
        }
    }

    private void fetchWishlist(long accountId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Product>> call = apiService.getWishlistByAccountId(accountId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    wishlistAdapter.updateList(response.body());
                } else {
                    Toast.makeText(WishlistActivity.this, "Failed to load wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(WishlistActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
