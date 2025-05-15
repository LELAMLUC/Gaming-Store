package com.gamingstore.store_api.controller;

import com.gamingstore.store_api.entity.Order;
import com.gamingstore.store_api.repository.OrderRepository;
import com.gamingstore.store_api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{accountId}/{subtotal}/{shippingFee}/{discount}/{total}/{deliveryMethod}")
    public boolean createOrder(
            @PathVariable Long accountId,
            @PathVariable double subtotal,
            @PathVariable double shippingFee,
            @PathVariable double discount,
            @PathVariable double total,
            @PathVariable String deliveryMethod
    ) {
        return orderService.createOrder(accountId, subtotal, shippingFee, discount, total, deliveryMethod);
    }
    // Thêm API lấy đơn hàng theo accountId
    @GetMapping("/take/{accountId}")
    public List<Order> getOrdersByAccountId(@PathVariable Long accountId) {
        return orderService.getOrdersByAccountId(accountId);
    }
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
}
