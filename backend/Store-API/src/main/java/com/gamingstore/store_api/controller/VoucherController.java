package com.gamingstore.store_api.controller;


import com.gamingstore.store_api.entity.Voucher;
import com.gamingstore.store_api.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/apply/{code}")
    public ResponseEntity<?> applyVoucher(@PathVariable String code) {
        try {
            Voucher voucher = voucherService.applyVoucher(code);
            return ResponseEntity.ok(voucher);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e .getMessage());
        }
    }
}