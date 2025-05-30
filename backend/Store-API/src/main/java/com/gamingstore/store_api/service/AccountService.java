package com.gamingstore.store_api.service;
import com.gamingstore.store_api.entity.Account;
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
    public boolean createOrChangePassAccount(String fullname, String email, String password) {
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
                Account account = new Account(fullname, email, password);
                accountRepository.save(account);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean changePassword(String email, String password) {
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
    public Long loginAndGetUserId(String email, String pass) {
        // Kiểm tra thông tin tài khoản trong cơ sở dữ liệu
        Account account = accountRepository.findByEmailAndPassword(email, pass); // Tìm tài khoản theo email và mật khẩu

        if (account != null) {
            // Nếu đăng nhập thành công, trả về userId
            return account.getId(); // Giả sử Account có phương thức getId() để lấy userId
        } else {
            // Nếu đăng nhập không thành công, trả về null
            return null;
        }
    }
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }
}