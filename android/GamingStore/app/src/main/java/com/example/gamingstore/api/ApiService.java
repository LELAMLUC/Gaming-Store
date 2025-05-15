package com.example.gamingstore.api;

import com.example.gamingstore.model.Account;
import com.example.gamingstore.model.Address;
import com.example.gamingstore.model.CartItem;
import com.example.gamingstore.model.Category;
import com.example.gamingstore.model.Order;
import com.example.gamingstore.model.Product;
import com.example.gamingstore.model.Voucher;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
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
    @GET("api/addresses/{accountId}")
    Call<Address> getAddressesByAccountId(@Path("accountId") Long accountId);
    @POST("api/addresses/create-and-get/{accountId}/{name}/{phone}/{address}")
    Call<ResponseBody> createOrUpdateAddress(
            @Path("accountId") long accountId,
            @Path("name") String name,
            @Path("phone") String phone,
            @Path("address") String address
    );
    @GET("api/vouchers/apply/{code}")
    Call<Voucher> applyVoucher(@Path("code") String code);

    @POST("api/orders/create/{accountId}/{subtotal}/{shippingFee}/{discount}/{total}/{deliveryMethod}")
    Call<Boolean> createOrder(
            @Path("accountId") long accountId,
            @Path("subtotal") double subtotal,
            @Path("shippingFee") double shippingFee,
            @Path("discount") double discount,
            @Path("total") double total,
            @Path("deliveryMethod") String deliveryMethod
    );
    @GET("api/orders/take/{accountId}")
    Call<List<Order>> getOrdersByAccountId(@Path("accountId") long accountId);
    @GET("api/orders/{orderId}")
    Call<Order> getOrderById(@Path("orderId") long orderId);
    @GET("/api/wishlist/add/{accountId}/{productId}")
    Call<Void> addWishlist(@Path("accountId") long accountId, @Path("productId") long productId);

    @GET("/api/wishlist/remove/{accountId}/{productId}")
    Call<Void> removeWishlist(@Path("accountId") long accountId, @Path("productId") long productId);
    @GET("api/wishlist/{accountId}")
    Call<List<Product>> getWishlistByAccountId(@Path("accountId") long accountId);
    @GET("api/wishlist/check/{accountId}/{productId}")
    Call<Boolean> isWishlisted(@Path("accountId") long accountId, @Path("productId") long productId);
    @DELETE("api/cart/remove/{accountId}/{productId}/{color}")
    Call<Boolean> removeFromCart(
            @Path("accountId") long accountId,
            @Path("productId") long productId,
            @Path("color") String color
    );
}
