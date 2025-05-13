package com.example.gamingstore.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gamingstore.R;
import com.example.gamingstore.api.RetrofitClient;
import com.example.gamingstore.model.Account;
import com.example.gamingstore.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvFullName, tvEmail, tvPhone, tvFullAddress, tvLocation;
    private ImageView imgAccount;

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

        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);  // Nếu không tìm thấy, trả về -1

        if (accountId != -1) {
            // Gọi API và cập nhật dữ liệu
            fetchUserProfile(accountId);
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }

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
                    tvFullAddress.setText(account.getAddress());

                    // Lấy địa chỉ và chỉ hiển thị quận và thành phố
                    String fullAddress = account.getAddress();
                    String location = getLocationFromAddress(fullAddress);
                    tvLocation.setText(location);

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

    // Hàm xử lý lấy quận và thành phố từ địa chỉ đầy đủ
    private String getLocationFromAddress(String fullAddress) {
        String[] parts = fullAddress.split(", ");
        if (parts.length > 1) {
            String district = parts[parts.length - 2]; // Quận
            String city = parts[parts.length - 1]; // Thành phố
            return district + ", " + city;
        } else {
            return fullAddress; // Trường hợp không có quận và thành phố
        }
    }
}
