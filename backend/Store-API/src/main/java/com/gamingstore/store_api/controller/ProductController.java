package com.gamingstore.store_api.controller;


import com.gamingstore.store_api.entity.Product;
import com.gamingstore.store_api.service.ProductService;
import com.gamingstore.store_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @GetMapping("/top-sellers")
    public List<Product> getTopSellingProducts() {
        // Gọi service để lấy top 4 sản phẩm bán chạy nhất
        return productService.getTopSellingProducts();
    }
    @GetMapping("/search/{query}")
    public ResponseEntity<List<Product>> searchProducts(@PathVariable String query) {
        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/product-detail/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}