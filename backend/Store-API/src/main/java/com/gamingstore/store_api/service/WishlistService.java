package com.gamingstore.store_api.service;

import com.gamingstore.store_api.entity.Account;
import com.gamingstore.store_api.entity.Product;
import com.gamingstore.store_api.entity.Wishlist;
import com.gamingstore.store_api.repository.AccountRepository;
import com.gamingstore.store_api.repository.ProductRepository;
import com.gamingstore.store_api.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Wishlist> getWishlistByAccountId(Long accountId) {
        return wishlistRepository.findByAccountId(accountId);
    }

    public Wishlist addToWishlist(Long accountId, Long productId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Optional: Check trùng
        Optional<Wishlist> existing = wishlistRepository.findByAccountAndProduct(account, product);
        if (existing.isPresent()) {
            throw new RuntimeException("This product is already in the wishlist.");
        }

        Wishlist wishlist = new Wishlist(account, product);
        return wishlistRepository.save(wishlist);
    }

    public void removeFromWishlist(Long accountId, Long productId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Wishlist> existing = wishlistRepository.findByAccountAndProduct(account, product);
        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
        } else {
            throw new RuntimeException("Wishlist item not found");
        }
    }
    public boolean isProductInWishlist(Long accountId, Long productId) {
        return wishlistRepository.existsByAccountIdAndProductId(accountId, productId);
    }

}