package com.example.gamingstore.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.activity.AddressActivity;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.Account;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.model.Address;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvFullName, tvEmail, tvPhone, tvFullAddress, tvLocation, tvInfoPayment;
    private ImageView imgAccount, imgPayment;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Khởi tạo các view
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvFullAddress = view.findViewById(R.id.tvFullAddress);
        imgAccount = view.findViewById(R.id.imgAccount);
        tvLocation = view.findViewById(R.id.tvLocation);
        imgPayment = view.findViewById(R.id.imgPayment);
        tvInfoPayment = view.findViewById(R.id.tvInfoPayment);
        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);  // Nếu không tìm thấy, trả về -1

        if (accountId != -1) {
            // Gọi API và cập nhật dữ liệu
            fetchUserProfile(accountId);
            fetchAddressProfile(accountId);
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
        ImageButton btnEditAddress = view.findViewById(R.id.btnEditAddress);

        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void fetchUserProfile(long accountId) {
        // Lấy ApiService từ RetrofitClient
        ApiService apiService = RetrofitClient.getApiService();

        // Gọi API
        Call<Account> call = apiService.getAccountById(accountId);

        // Gửi yêu cầu API và xử lý kết quả
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Account account = response.body();
                    tvFullName.setText(account.getFullName());
                    tvEmail.setText(account.getEmail());
                    tvPhone.setText(account.getPhone());
                    if(account.getPaymentMethod()!=0){
                        imgPayment.setBackgroundResource(R.drawable.momo);
                        tvInfoPayment.setText(account.getPhone());
                    }
                    // Đặt ảnh người dùng nếu có URL
                    if (account.getAvatarUrl() != null) {
                        Glide.with(ProfileFragment.this)
                                .load(account.getAvatarUrl())
                                .into(imgAccount);
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể lấy thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                // Xử lý khi gọi API thất bại
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchAddressProfile(long accountId) {
        ApiService apiService = RetrofitClient.getApiService();

        // Gọi API
        Call<Address> call = apiService.getAddressesByAccountId(accountId);

        // Gửi yêu cầu API và xử lý kết quả
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Address address = response.body();


                    String fullAddress = address.getAddress();
                    String safeAddress = fullAddress.replace("_", "/");
                    tvFullAddress.setText(safeAddress);
                    String location = getLocationFromAddress(safeAddress);
                    tvLocation.setText(location);

                } else {
                    Toast.makeText(getContext(), "Không thể lấy thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                // Xử lý khi gọi API thất bại
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ProfileFragment", "Lỗi kết nối: " + t.getMessage(), t);
            }
        });
    }
    // Hàm xử lý lấy quận và thành phố từ địa chỉ đầy đủ
    private String getLocationFromAddress(String fullAddress) {
        String[] parts = fullAddress.split(",");
        if (parts.length == 4) {
            String district = parts[2].trim(); // quận/huyện
            String city = parts[3].trim();     // tỉnh/thành phố
            return district + ", " + city;
        } else {
            return "Địa chỉ không hợp lệ";
        }
    }

}


