package com.example.gamingstore.api;

import com.example.gamingstore.model.Account;
import com.example.gamingstore.model.CartItem;
import com.example.gamingstore.model.Category;
import com.example.gamingstore.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/accounts/check_email/{email}")
    Call<Boolean> checkEmailExist(@Path("email") String email);

    @POST("api/otp_request/generate/{email}")
    Call<Boolean> generateOTP(@Path("email") String email);

    @POST("api/otp_request/verify/{email}/{otp}")
    Call<Boolean> verifyOTP(@Path("email") String email, @Path("otp") String otp);

    @POST("api/accounts/createOrChangePassAccount/{fullname}/{email}/{pass}")
    Call<Boolean> createOrChangePassAccount(@Path("fullname") String name, @Path("email") String email, @Path("pass") String pass);

    @POST("api/accounts/login/{email}/{pass}")
    Call<Long> login(@Path("email") String email, @Path("pass") String pass);


    @POST("/changePassword/{email}/{pass}")
    Call<Boolean> changePassword(@Path("email") String email, @Path("pass") String password);

    // ✅ Lấy tất cả category
    @GET("api/categories/takeAll")
    Call<List<Category>> getCategories();

    // ✅ Lấy tất cả sản phẩm
    @GET("api/products/allproducts")
    Call<List<Product>> getAllProducts();

    // ✅ Lấy sản phẩm theo danh mục
    @GET("api/products/category/{categoryId}")
    Call<List<Product>> getProductsByCategory(@Path("categoryId") long categoryId);
    @GET("api/products/popular")
    Call<List<Product>> getPopularProducts();
    @GET("/api/products/top-sellers")
    Call<List<Product>> getTopSellingProducts();

    @GET("api/products/search/{query}")
    Call<List<Product>> searchProducts(@Path("query") String query);
    @GET("api/products/product-detail/{id}")
    Call<Product> getProductById(@Path("id") Long id);
    @POST("api/cart/add/{accountId}/{productId}/{quantity}/{color}")
    Call<Boolean> addToCart(
            @Path("accountId") long accountId,
            @Path("productId") long productId,
            @Path("quantity") int quantity,
            @Path("color") String color
    );
    @GET("api/cart/getItemCart/{accountId}")
    Call<List<CartItem>> getCartItems(@Path("accountId") long accountId);
    @GET("api/accounts/takeProfile/{id}")
    Call<Account> getAccountById(@Path("id") Long id);
}
