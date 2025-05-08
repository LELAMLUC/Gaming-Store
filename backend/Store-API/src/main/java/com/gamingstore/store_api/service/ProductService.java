package com.gamingstore.store_api.service;


import com.gamingstore.store_api.entity.Product;
import com.gamingstore.store_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    public List<Product> getProductsByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    public List<Product> getPopularProducts() {
        return productRepository.findTop6ByRating();
    }
    public List<Product> getTopSellingProducts() {
        // Lấy tất cả các sản phẩm từ database
        List<Product> allProducts = productRepository.findAll();

        // Sắp xếp sản phẩm theo số lượng bán (soldQuantity) và lấy top 4 sản phẩm bán chạy nhất
        return allProducts.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getSoldQuantity(), p1.getSoldQuantity())) // Sắp xếp giảm dần theo soldQuantity
                .limit(4) // Lấy 4 sản phẩm đầu tiên
                .collect(Collectors.toList());
    }
    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}