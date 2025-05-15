package com.gamingstore.store_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final String vnp_TmnCode = "U8Z06KJR"; // Mã website của bạn trên VNPAY Sandbox
    private final String vnp_HashSecret = "TMTNTJRG0D7JETWOCZJB41NNSYO8CXPD"; // Secret key sandbox
    private final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "https://github.com/LELAMLUC"; // Thay bằng URL công khai chạy được

    @GetMapping("/create")
    public ResponseEntity<String> createPayment() {
        long amount = 10000L; // 10.000 VNĐ

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // 10.000 * 100 = 1.000.000
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String key : fieldNames) {
            String value = vnp_Params.get(key);
            hashData.append(key).append('=').append(value).append('&');
            query.append(key).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8)).append('&');
        }

        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());

        String paymentUrl = vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + secureHash;

        return ResponseEntity.ok(paymentUrl);
    }
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating HMAC SHA512", e);
        }
    }

    @GetMapping("/return")
    public ResponseEntity<String> paymentReturn(@RequestParam Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            return ResponseEntity.ok("Thanh toán thành công!");
        } else {
            return ResponseEntity.ok("Thanh toán thất bại hoặc bị hủy.");
        }
    }
}
