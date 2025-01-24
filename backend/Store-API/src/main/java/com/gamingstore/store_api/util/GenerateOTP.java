package com.gamingstore.store_api.util;

import java.util.Random;

public class GenerateOTP {

    // Hàm tạo OTP ngẫu nhiên
    public static String generateOTP() {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);  // Tạo số OTP 6 chữ số
        return String.valueOf(otp);
    }
}