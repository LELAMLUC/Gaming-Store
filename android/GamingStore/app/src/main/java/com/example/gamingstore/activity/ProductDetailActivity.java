package com.example.gamingstore.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gamingstore.R;
import com.example.gamingstore.model.Product;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvNameProduct, tvPrice, tvPrice2, tvStock, tvDiscount, tvDescription;
    private ImageView imgProduct, imgColor, imageView52, imageView76, btnMinus, btnPlus, btnCart, btnBuyNow;
    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        tvNameProduct = findViewById(R.id.tvNameProduct);
        tvPrice = findViewById(R.id.tvPrice);
        tvPrice2 = findViewById(R.id.tvPrice2);
        tvStock = findViewById(R.id.tvStock);
        tvDiscount = findViewById(R.id.tvDiscount);
        imgProduct = findViewById(R.id.imgProduct);
        imgColor = findViewById(R.id.imgColor);
        imageView52 = findViewById(R.id.imageView52);
        imageView76 = findViewById(R.id.imageView76);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnCart = findViewById(R.id.btnCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        // Get product ID from Intent
        productId = getIntent().getLongExtra("product_id", productId);

        // Call API to fetch product details
        getProductDetail(productId);
    }

    private void getProductDetail(long productId) {
        Log.d("API Call", "Calling API with ID: " + productId);

        // Use Retrofit to fetch product details
        RetrofitClient.getApiService().getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();

                    // Set product details into the views
                    tvNameProduct.setText(product.getName());
                    tvPrice.setText("$" + product.getPrice());

                    // Calculate discounted price
                    double discountAmount = product.getPrice() * product.getDiscountPercent() / 100;
                    double discountedPrice = product.getPrice() - discountAmount;
                    tvPrice2.setText("$" + discountedPrice);
                    tvStock.setText("In Stock (" + product.getQuantity() + ")");
                    tvDiscount.setText(product.getDiscountPercent() + "% OFF");

                    // Load product image using Glide
                    Glide.with(ProductDetailActivity.this)
                            .load(product.getImageUrl())  // Assuming product has imageUrl
                            .into(imgProduct);

                    // You can also load additional images for color options if needed:
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Handle failure, show error message or retry logic
            }
        });
    }
}
