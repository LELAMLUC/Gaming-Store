package com.gamingstore.store_api.controller;

import com.gamingstore.store_api.entity.Cart;
import com.gamingstore.store_api.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add/{accountId}/{productId}/{quantity}/{color}")
    public boolean addToCart(
            @PathVariable Long accountId,
            @PathVariable Long productId,
            @PathVariable int quantity,
            @PathVariable String color
    ) {
        cartService.addToCart(accountId, productId, quantity, color);
        return true;  // Trả về true khi sản phẩm được thêm vào giỏ
    }
    @GetMapping("/getItemCart/{accountId}")
    public ResponseEntity<List<Cart>> getCartByAccountId(@PathVariable Long accountId) {
        List<Cart> cartList = cartService.getCartByAccountId(accountId);
        return ResponseEntity.ok(cartList);
    }

    @DeleteMapping("/remove/{accountId}/{productId}/{color}")
    public ResponseEntity<Boolean> removeItemFromCart(
            @PathVariable Long accountId,
            @PathVariable Long productId,
            @PathVariable String color) {

        cartService.removeItemFromCart(accountId, productId, color);
        return ResponseEntity.ok(true);
    }
}
