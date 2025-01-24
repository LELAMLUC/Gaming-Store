package com.gamingstore.store_api.service;

import com.gamingstore.store_api.model.OtpRequest;
import com.gamingstore.store_api.repository.OtpRequestRepository;
import com.gamingstore.store_api.util.EmailSender;
import com.gamingstore.store_api.util.GenerateOTP;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OtpRequestService {
    private final OtpRequestRepository otpRequestRepository;
    private final EmailSender emailSender;

    public OtpRequestService(OtpRequestRepository otpRequestRepository, EmailSender emailSender) {
        this.otpRequestRepository = otpRequestRepository;
        this.emailSender = emailSender;
    }

    // Tạo và lưu OTP vào database
    public String generateAndSaveOTP(String email) {
        // Tạo otp
        String otp = GenerateOTP.generateOTP();

        // Thiết lập thời gian hết hạn 5p
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plus(5, ChronoUnit.MINUTES);

        // Kiểm tra nếu email đã tồn tại trong bảng otp_request
        List<OtpRequest> existingOtpRequests = otpRequestRepository.findByEmail(email);

        // Cập nhập lại otp nếu mail đã tồn tại
        if (!existingOtpRequests.isEmpty()) {
            OtpRequest otpRequest = existingOtpRequests.get(0);
            otpRequest.setOtpCode(otp);
            otpRequest.setExpiresAt(expiresAt);
            otpRequest.setCreatedAt(now);
            otpRequest.setIsUsed(false);
            otpRequestRepository.save(otpRequest);
        }
        else {
            // Nếu không tồn tại, tạo bản ghi mới và truyền đầy đủ tham số
            OtpRequest otpRequest = new OtpRequest(email, otp, expiresAt, now);
            otpRequest.setIsUsed(false);
            otpRequestRepository.save(otpRequest);
        }
        return otp;
    }
    // Gửi OTP cho Email người nhận
    public void SendOtpToEmail(String email, String otp) {
        emailSender.sendOTP(email,otp);
    }

    public boolean verifyOTP(String email, String enteredOTP) {
        OtpRequest otpRequest = otpRequestRepository.findByEmailAndIsUsedFalse(email);
        if (otpRequest != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expiresAt = otpRequest.getExpiresAt();
            // kiểm tra hết hạn
            if (currentTime.isAfter(expiresAt)) {
                return false;
            }
            // kiểm tra trùng otp
            else if (otpRequest.getOtpCode().equals(enteredOTP)) {
                otpRequest.setIsUsed(true);
                otpRequestRepository.save(otpRequest);
                return true;
            }
        }
        return false;
    }
}
