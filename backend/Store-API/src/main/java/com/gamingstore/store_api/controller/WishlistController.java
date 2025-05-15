package com.gamingstore.store_api.controller;

import com.gamingstore.store_api.entity.Product;
import com.gamingstore.store_api.entity.Wishlist;
import com.gamingstore.store_api.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getWishlistByAccountId(@PathVariable Long accountId) {
        List<Wishlist> wishlist = wishlistService.getWishlistByAccountId(accountId);
        // Chuyển Wishlist sang Product
        List<Product> products = wishlist.stream()
                .map(Wishlist::getProduct)
                .toList();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/add/{accountId}/{productId}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long accountId, @PathVariable Long productId) {
        try {
            Wishlist wishlist = wishlistService.addToWishlist(accountId, productId);
            return ResponseEntity.ok(wishlist);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/remove/{accountId}/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long accountId, @PathVariable Long productId) {
        try {
            wishlistService.removeFromWishlist(accountId, productId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/check/{accountId}/{productId}")
    public ResponseEntity<Boolean> isProductInWishlist(@PathVariable Long accountId, @PathVariable Long productId) {
        boolean exists = wishlistService.isProductInWishlist(accountId, productId);
        return ResponseEntity.ok(exists);
    }
}
