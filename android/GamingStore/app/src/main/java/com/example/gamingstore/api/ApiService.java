package com.example.gamingstore.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Kiểm tra email có tồn tại không
    @POST("api/accounts/check_email/{email}")
    Call<Boolean> checkEmailExist(@Path("email") String email);

    // Tạo và gửi OTP
    @POST("api/otp_request/generate/{email}")
    Call<Boolean> generateOTP(@Path("email") String email);

    @POST("api/otp_request/verify/{email}/{otp}")
    Call<Boolean> verifyOTP(@Path("email") String email, @Path("otp") String otp);

    @POST("api/accounts/createOrChangePassAccount/{email}/{pass}")
    Call<Boolean> createOrChangePassAccount(@Path("email") String email, @Path("pass") String pass);

    // Kiểm tra Login ? true:false
    @POST("api/accounts/login/{email}/{pass}")
    Call<Boolean> login(@Path("email") String email,@Path("pass") String pass) ;
}