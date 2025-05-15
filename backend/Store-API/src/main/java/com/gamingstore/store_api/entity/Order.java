package com.gamingstore.store_api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")  // tên số nhiều
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double subtotal;
    private double shippingFee;
    private double discount;
    private double total;

    private String deliveryMethod;

    private LocalDateTime orderDate;

    // Lưu lại địa chỉ đơn hàng (copy thông tin, không tham chiếu Address entity)
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Các sản phẩm trong đơn (OrderItem)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;

    // Constructor không tham số (bắt buộc cho JPA)
    public Order() {}

    public void setShippingInfo(String name, String phone, String address) {
        this.shippingName = name;
        this.shippingPhone = phone;
        this.shippingAddress = address;
    }
    // Constructor đầy đủ
    public Order(Long id, double subtotal, double shippingFee, double discount, double total, String deliveryMethod,
                 LocalDateTime orderDate, String shippingName, String shippingPhone, String shippingAddress,
                 Account account, List<OrderItem> items) {
        this.id = id;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discount = discount;
        this.total = total;
        this.deliveryMethod = deliveryMethod;
        this.orderDate = orderDate;
        this.shippingName = shippingName;
        this.shippingPhone = shippingPhone;
        this.shippingAddress = shippingAddress;
        this.account = account;
        this.items = items;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
