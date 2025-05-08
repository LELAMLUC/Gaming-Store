package com.gamingstore.store_api.controller;

import com.gamingstore.store_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    // Kiểm tra Login ? true:false
    @PostMapping("/login/{email}/{pass}")
    public boolean login(@PathVariable String email,@PathVariable String pass) {
        // Gọi dịch vụ để kiểm tra email trong bảng Account
        return accountService.login(email,pass);  // Trả về true nếu email tồn tại, false nếu không
    }

    // kiểm tra email đã tồn tại? True : False
    @PostMapping("/check_email/{email}")
    public boolean checkEmailExist(@PathVariable String email) {
        // Gọi dịch vụ để kiểm tra email trong bảng Account
        return accountService.isEmailExist(email);  // Trả về true nếu email tồn tại, false nếu không
    }

    // Đăng Kí Hoặc Đổi PASS tài khoản ? true :false
    @PostMapping("/createOrChangePassAccount/{fullname}/{email}/{pass}")
    public boolean createOrChangePassAccount(@PathVariable String fullname, @PathVariable String email, @PathVariable String pass) {
        return accountService.createOrChangePassAccount(fullname,email, pass);
    }
    @PostMapping("/changePassword/{email}/{pass}")
    public boolean changePassword(@PathVariable String email, @PathVariable String pass) {
        return accountService.changePassword(email, pass);
    }
}
