package com.gamingstore.store_api.repository;

import com.gamingstore.store_api.entity.Cart;
import com.gamingstore.store_api.entity.Account;
import com.gamingstore.store_api.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByAccountId(Long accountId);
    // Phương thức này để tìm cart theo account, product và color
    Optional<Cart> findByAccountAndProductAndColor(Account account, Product product, String color);
    List<Cart> findByAccount(Account account);
    @Transactional
    @Modifying
    void deleteByAccountIdAndProductIdAndColor(Long accountId, Long productId, String color);

}
