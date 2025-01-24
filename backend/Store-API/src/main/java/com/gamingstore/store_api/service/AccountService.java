package com.gamingstore.store_api.service;
import com.gamingstore.store_api.model.Account;
import com.gamingstore.store_api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    // tìm email đã tồn tại? true:false
    public boolean isEmailExist(String email) {
        return accountRepository.existsByEmail(email);
    }
    // Tạo tài khoản trả về ? true:false
    public boolean createOrChangePassAccount(String email, String password) {
        try {
            // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
            Optional<Account> existingAccount = accountRepository.findByEmail(email);

            if (existingAccount.isPresent()) {
                // Nếu email đã tồn tại, cập nhật mật khẩu
                Account account = existingAccount.get();
                account.setPassword(password);
                accountRepository.save(account);
                return true;
            } else {
                // Nếu email chưa tồn tại, tạo tài khoản mới
                Account account = new Account(email, password);
                accountRepository.save(account);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean login(String email, String password) {
        try {
            // Tìm kiếm tài khoản theo email trong cơ sở dữ liệu
            Optional<Account> accountOptional = accountRepository.findByEmail(email);

            // Kiểm tra tài khoản có tồn tại không
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                // Kiểm tra mật khẩu có khớp không
                if (account.getPassword().equals(password)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}