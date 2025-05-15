package com.gamingstore.store_api.repository;

import com.gamingstore.store_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Bạn có thể thêm các method tùy chỉnh nếu cần
    List<Order> findByAccountId(Long accountId);
}
