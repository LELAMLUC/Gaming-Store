package com.gamingstore.store_api.service;

import com.gamingstore.store_api.entity.Voucher;
import com.gamingstore.store_api.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher applyVoucher(String code) {
        Optional<Voucher> optionalVoucher = voucherRepository.findByCode(code);

        if (optionalVoucher.isEmpty()) {
            throw new RuntimeException("Voucher không tồn tại");
        }

        Voucher voucher = optionalVoucher.get();

        if (!voucher.isActive()) {
            throw new RuntimeException("Voucher đã bị vô hiệu hóa");
        }

        if (voucher.getQuantity() <= 0) {
            throw new RuntimeException("Voucher đã hết lượt sử dụng");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(voucher.getStartDate()) || now.isAfter(voucher.getEndDate())) {
            throw new RuntimeException("Voucher không còn trong thời gian hiệu lực");
        }

        return voucher;
    }
}
