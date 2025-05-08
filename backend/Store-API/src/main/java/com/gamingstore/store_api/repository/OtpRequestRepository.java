package com.gamingstore.store_api.repository;

import com.gamingstore.store_api.entity.OtpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRequestRepository extends JpaRepository<OtpRequest, Long> {
    List<OtpRequest> findByEmail(String email);  // Tìm kiếm OTP theo email
    OtpRequest findByEmailAndIsUsedFalse(String email);
}
