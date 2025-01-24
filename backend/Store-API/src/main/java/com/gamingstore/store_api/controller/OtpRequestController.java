package com.gamingstore.store_api.controller;

import com.gamingstore.store_api.model.OtpRequest;
import com.gamingstore.store_api.service.OtpRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/otp_request")
public class OtpRequestController {

    private final OtpRequestService otpRequestService;

    public OtpRequestController(OtpRequestService otpRequestService) {
        this.otpRequestService = otpRequestService;
    }

    // Gửi OTP và Mail ? true:false
    @PostMapping("/generate/{email}")
    public boolean generateOTP(@PathVariable String email) {
        try {
            // Tạo OTP và lưu vào cơ sở dữ liệu
            String otp = otpRequestService.generateAndSaveOTP(email);
            // Gửi otp qua mail
            otpRequestService.SendOtpToEmail(email, otp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // Check OTP Người dùng nhập vào với otp trong database
    @PostMapping("/verify/{email}/{otp}")
    public boolean verifyOTP(@PathVariable String email, @PathVariable String otp) {
        return otpRequestService.verifyOTP(email, otp);
    }

}