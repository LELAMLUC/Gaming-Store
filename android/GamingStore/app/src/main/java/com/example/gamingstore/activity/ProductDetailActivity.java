package com.example.gamingstore.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gamingstore.R;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.fragment.CartFragment;
import com.example.gamingstore.model.Product;
import com.example.gamingstore.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends BaseActivity {

    private TextView tvNameProduct, tvPrice, tvPrice2, tvStock, tvDiscount, tvDescription,tvQuantity;
    private ImageView imgProduct, imageView52, imageView76, btnMinus, btnPlus, btnCart, btnBuyNow, btnAddToCart,btnHeart;
    private LinearLayout imgColor;
    private boolean isWishlisted = false; // trạng thái thích hay chưa
    private long productId;
    private View selectedColorView = null;  // Để lưu trữ màu đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        EdgeToEdge.enable(this);
        // Initialize views
        tvNameProduct = findViewById(R.id.tvNameProduct);
        tvPrice = findViewById(R.id.tvQuantity);
        tvPrice2 = findViewById(R.id.tvPrice2);
        tvStock = findViewById(R.id.tvStock);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvDescription=findViewById(R.id.tvDes);
        imgProduct = findViewById(R.id.imgProduct);
        imgColor = findViewById(R.id.imgColor);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnCart = findViewById(R.id.btnCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        tvQuantity = findViewById(R.id.tvQuantityNumber);
        btnAddToCart=findViewById(R.id.btnAddToCart);
        // Get product ID from Intent
        productId = getIntent().getLongExtra("productId", productId);

        // Call API to fetch product details
        getProductDetail(productId);
        btnPlus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnMinus.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });
        btnCart.setOnClickListener(v -> {
            // Mở CartFragment ngay trong activity hiện tại
            findViewById(R.id.productDetailFragmentContainer).setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.productDetailFragmentContainer, new CartFragment())
                    .addToBackStack(null)
                    .commit();
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);  // Nếu không tìm thấy, trả về -1
        if (accountId != -1) {
            checkWishlistStatus(accountId, productId);
        }

        btnAddToCart.setOnClickListener(v -> {
            // Lấy accountId từ SharedPreferences
            if (accountId == -1) {
                // Nếu không tìm thấy accountId (chưa đăng nhập), có thể hiển thị thông báo hoặc yêu cầu đăng nhập
                Toast.makeText(ProductDetailActivity.this, "Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
                return;
            }
            // Lấy các giá trị cần thiết từ UI
            int quantity = Integer.parseInt(tvQuantity.getText().toString());

            if (selectedColorView == null) {
                Toast.makeText(ProductDetailActivity.this, "Please select a color!", Toast.LENGTH_SHORT).show();
                return;
            }

            GradientDrawable selectedDrawable = (GradientDrawable) selectedColorView.getBackground();
            int colorInt = selectedDrawable.getColor().getDefaultColor();
            String selectedColorHex = String.format("#%06X", (0xFFFFFF & colorInt));

            // In log để kiểm tra dữ liệu gửi đi
            ApiService apiService = RetrofitClient.getApiService();
            Call<Boolean> call = apiService.addToCart(accountId, productId, quantity, selectedColorHex);

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        Toast.makeText(ProductDetailActivity.this, "Đã thêm sản phẩm vào giỏ!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Thêm giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("AddToCart", "API error", t);
                    Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối tới server", Toast.LENGTH_SHORT).show();
                }
            });
        });
        btnHeart = findViewById(R.id.btnHeart);

// Giả sử bạn có thể lấy trạng thái wishlist ban đầu từ API hoặc intent
// Nếu chưa có, mặc định là chưa thích:
        updateHeartIcon();

        btnHeart.setOnClickListener(v -> {
            isWishlisted = !isWishlisted;
            updateHeartIcon();

            // Lấy accountId từ SharedPreferences
            if (accountId == -1) {
                Toast.makeText(ProductDetailActivity.this, "Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
                // Quay về trạng thái ban đầu
                isWishlisted = !isWishlisted;
                updateHeartIcon();
                return;
            }

            if (isWishlisted) {
                addToWishlist(accountId, productId);
            } else {
                removeFromWishlist(accountId, productId);
            }
        });
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
                    tvPrice2.setText("$" + product.getPrice());
                    tvPrice2.setPaintFlags(tvPrice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    // Calculate discounted price
                    double discountAmount = product.getPrice() * product.getDiscountPercent() / 100;
                    double discountedPrice = product.getPrice() - discountAmount;
                    String formattedPrice = String.format("%.2f", discountedPrice);
                    tvPrice.setText("$" + formattedPrice);
                    tvStock.setText("In Stock (" + product.getQuantity() + ")");
                    tvDiscount.setText(product.getDiscountPercent() + "% OFF");
                    tvDescription.setText(product.getDescription());
                    // Load product image using Glide
                    Glide.with(ProductDetailActivity.this)
                            .load(product.getImageUrl())  // Assuming product has imageUrl
                            .into(imgProduct);

                    // Example colors, you can replace this with actual data from API
                    String colorString = product.getColors();
                    String[] colorList = colorString.split(",");

                    LinearLayout colorLayout = findViewById(R.id.imgColor);
                    colorLayout.removeAllViews(); // Clear nếu có sẵn

                    for (String colorCode : colorList) {
                        View colorView = new View(ProductDetailActivity.this);

                        // Kích thước & margin cho từng ô màu
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(135, 135); // 135dp cho mỗi ô màu
                        params.setMargins(16, 0, 30, 0);  // Khoảng cách giữa các màu
                        colorView.setLayoutParams(params);

                        // Đặt màu nền cho view
                        try {
                            colorView.setBackgroundColor(Color.parseColor(colorCode.trim()));
                        } catch (IllegalArgumentException e) {
                            Log.e("ColorParse", "Lỗi parse màu: " + colorCode);
                        }

                        // Nếu muốn làm tròn hình
                        GradientDrawable shape = new GradientDrawable();
                        shape.setShape(GradientDrawable.OVAL);
                        shape.setColor(Color.parseColor(colorCode.trim()));
                        colorView.setBackground(shape);

                        // Thêm sự kiện khi người dùng chọn màu
                        colorView.setOnClickListener(v -> onColorSelected(colorView, colorCode.trim()));

                        // Add vào layout
                        colorLayout.addView(colorView);
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Handle failure, show error message or retry logic
            }
        });
    }

    private void onColorSelected(View colorView, String colorCode) {
        // Nếu đã có màu được chọn, hãy loại bỏ border
        if (selectedColorView != null) {
            // Loại bỏ border cũ
            GradientDrawable shape = (GradientDrawable) ((View) selectedColorView).getBackground();
            shape.setStroke(0, Color.TRANSPARENT);
        }

        // Thêm border cho màu đã chọn
        GradientDrawable shape = (GradientDrawable) colorView.getBackground();
        shape.setStroke(4, Color.parseColor("#0ABDE3")); // Đặt màu viền

        // Lưu lại màu đã chọn
        selectedColorView = colorView;
    }
    private void updateHeartIcon() {
        if (isWishlisted) {
            btnHeart.setBackgroundResource(R.drawable.icon_heart2);
        } else {
            btnHeart.setBackgroundResource(R.drawable.icon_heart1);
        }
    }
    private void addToWishlist(long accountId, long productId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.addWishlist(accountId, productId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Thêm yêu thích thất bại", Toast.LENGTH_SHORT).show();
                    // Rollback trạng thái
                    isWishlisted = false;
                    updateHeartIcon();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                isWishlisted = false;
                updateHeartIcon();
            }
        });
    }

    private void removeFromWishlist(long accountId, long productId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.removeWishlist(accountId, productId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Bỏ yêu thích thất bại", Toast.LENGTH_SHORT).show();
                    // Rollback trạng thái
                    isWishlisted = true;
                    updateHeartIcon();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                isWishlisted = true;
                updateHeartIcon();
            }
        });
    }
    private void checkWishlistStatus(long accountId, long productId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.isWishlisted(accountId, productId);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    isWishlisted = true;
                    updateHeartIcon();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("WishlistCheck", "Lỗi kết nối API", t);
            }
        });
    }

}
