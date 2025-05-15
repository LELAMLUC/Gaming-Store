package com.gamingstore.store_api.repository;

import com.gamingstore.store_api.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Bạn có thể thêm các method tùy chỉnh nếu cần
}
