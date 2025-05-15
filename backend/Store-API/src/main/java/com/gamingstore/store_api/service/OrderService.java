package com.gamingstore.store_api.service;

import com.gamingstore.store_api.entity.*;
import com.gamingstore.store_api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public boolean createOrder(Long accountId,
                               double subtotal,
                               double shippingFee,
                               double discount,
                               double total,
                               String deliveryMethod) {
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            List<Cart> cartItems = cartRepository.findByAccount(account);
            if (cartItems.isEmpty()) {
                return false;
            }

            Address address = addressRepository.findByAccountId(accountId);
            if (address == null) {
                return false;
            }

            Order order = new Order();
            order.setAccount(account);
            order.setSubtotal(subtotal);
            order.setShippingFee(shippingFee);
            order.setDiscount(discount);
            order.setTotal(total);
            order.setDeliveryMethod(deliveryMethod);
            order.setOrderDate(LocalDateTime.now());

            order.setShippingName(address.getName());
            order.setShippingPhone(address.getPhone());
            order.setShippingAddress(address.getAddress());

            orderRepository.save(order);

            for (Cart cart : cartItems) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductName(cart.getProduct().getName());
                item.setProductPrice(cart.getProduct().getPrice());
                item.setProductColor(cart.getColor());
                item.setQuantity(cart.getQuantity());

                // Gắn thêm URL ảnh sản phẩm
                item.setProductImage(cart.getProduct().getImageUrl());

                orderItemRepository.save(item);
            }

//            cartRepository.deleteAll(cartItems);

            return true;
        } catch (Exception e) {
            // Bạn có thể log lỗi nếu cần
            return false;
        }
    }
    public List<Order> getOrdersByAccountId(Long accountId) {
        return orderRepository.findByAccountId(accountId);
    }
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
