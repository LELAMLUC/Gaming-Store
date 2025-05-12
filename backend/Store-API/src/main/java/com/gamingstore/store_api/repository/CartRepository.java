package com.gamingstore.store_api.repository;

import com.gamingstore.store_api.entity.Cart;
import com.gamingstore.store_api.entity.Account;
import com.gamingstore.store_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Sửa lại phương thức này
    List<Cart> findByAccount(Account account);

    // Phương thức này để tìm cart theo account, product và color
    Optional<Cart> findByAccountAndProductAndColor(Account account, Product product, String color);
}
