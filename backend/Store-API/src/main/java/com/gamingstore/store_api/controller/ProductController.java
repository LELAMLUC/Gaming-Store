package com.gamingstore.store_api.controller;


import com.gamingstore.store_api.entity.Product;
import com.gamingstore.store_api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Lấy tất cả sản phẩm
    @GetMapping("/allproducts")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Lấy sản phẩm theo ID
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable int categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
    // Lấy sản phẩm phổ biến theo rating cao nhất (6 sản phẩm)
    @GetMapping("/popular")
    public List<Product> getPopularProducts() {
        return productService.getPopularProducts();
    }
}