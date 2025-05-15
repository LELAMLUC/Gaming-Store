package com.gamingstore.store_api.repository;

import com.gamingstore.store_api.entity.Account;
import com.gamingstore.store_api.entity.Product;
import com.gamingstore.store_api.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByAccountId(Long accountId);
    Optional<Wishlist> findByAccountAndProduct(Account account, Product product);
    boolean existsByAccountIdAndProductId(Long accountId, Long productId);

}
