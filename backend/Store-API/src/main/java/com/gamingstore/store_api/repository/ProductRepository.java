package com.gamingstore.store_api.repository;


import com.gamingstore.store_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh ở đây
    List<Product> findByCategoryId(int categoryId);
    @Query("SELECT p FROM Product p ORDER BY p.rating DESC")
    List<Product> findTop6ByRating();
    List<Product> findByNameContainingIgnoreCase(String name);
}
