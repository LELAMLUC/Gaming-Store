package com.gamingstore.store_api.service;

import com.gamingstore.store_api.entity.Account;
import com.gamingstore.store_api.entity.Cart;
import com.gamingstore.store_api.entity.Product;
import com.gamingstore.store_api.repository.AccountRepository;
import com.gamingstore.store_api.repository.CartRepository;
import com.gamingstore.store_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long userId, Long productId, int quantity, String color) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Kiểm tra xem người dùng đã có sản phẩm này trong giỏ chưa (cùng màu)
        Optional<Cart> existingCart = cartRepository.findByAccountAndProductAndColor(account, product, color);

        if (existingCart.isPresent()) {
            // Nếu có thì tăng số lượng
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cartRepository.save(cart);
        } else {
            // Nếu chưa thì tạo mới
            Cart cart = new Cart();
            cart.setAccount(account);
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setColor(color);
            cartRepository.save(cart);
        }
    }
    public List<Cart> getCartByAccountId(Long accountId) {
        return cartRepository.findByAccountId(accountId);
    }
}
