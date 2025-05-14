package com.example.gamingstore.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamingstore.R;
import com.example.gamingstore.api.ApiService;
import com.example.gamingstore.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);

        // Lấy accountId từ SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long accountId = sharedPreferences.getLong("accountId", -1);

        // Khai báo EditText và Button
        EditText edtName = findViewById(R.id.edtName);
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtCity = findViewById(R.id.edtCity);
        EditText edtDistrict = findViewById(R.id.edtDistrict);
        EditText edtWard = findViewById(R.id.edtWard);
        EditText edtAddress = findViewById(R.id.edtAddress);
        ImageButton btnRegister = findViewById(R.id.btnRegister4);

        // Dùng RetrofitClient để lấy instance của AddressApi
        ApiService addressApi = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        btnRegister.setOnClickListener(v -> {
            // Lấy dữ liệu từ các EditText
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String city = edtCity.getText().toString().trim();
            String district = edtDistrict.getText().toString().trim();
            String ward = edtWard.getText().toString().trim();
            String detail = edtAddress.getText().toString().trim();

            // Kiểm tra xem có trường nào trống không
            if (name.isEmpty() || phone.isEmpty() || city.isEmpty() || district.isEmpty() || ward.isEmpty() || detail.isEmpty()) {
                // Hiển thị thông báo lỗi nếu có trường trống
                Toast.makeText(AddressActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;  // Dừng việc gửi dữ liệu nếu có trường trống
            }
            String fullAddress = detail + ", " + ward + ", " + district + ", " + city;
            String safeAddress = fullAddress.replace("/", "_");
            Call<ResponseBody> call = addressApi.createOrUpdateAddress(accountId, name, phone, safeAddress);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string().trim();
                            Log.d("AddressAPI", "Raw response: " + result);

                            // Kiểm tra nếu phản hồi chứa thông báo thành công
                            if (result.contains("Địa chỉ đã được lưu thành công")) {
                                Toast.makeText(AddressActivity.this, "Lưu địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddressActivity.this, "Không thể lưu địa chỉ.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("AddressAPI", "Lỗi xử lý phản hồi: " + e.getMessage());
                        }
                    } else {
                        Log.e("AddressAPI", "Lỗi phản hồi server: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("AddressAPI", "Lỗi kết nối: ", t);
                }
            });


        });
    }
}
