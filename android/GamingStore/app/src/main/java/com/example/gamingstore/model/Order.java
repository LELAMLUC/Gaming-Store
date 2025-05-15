package com.example.gamingstore.model;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private long id;
    private double subtotal;
    private double shippingFee;
    private double discount;
    private double total;
    private String deliveryMethod;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String orderDate; // Dùng String nếu không cần xử lý DateTime phức tạp
    private List<OrderItem> items;
    public Order() {
        // default constructor
    }
    public Order(long id, double subtotal, double shippingFee, double discount, double total,
                 String deliveryMethod, String shippingName, String shippingPhone, String shippingAddress,
                 String orderDate, List<OrderItem> items) {
        this.id = id;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discount = discount;
        this.total = total;
        this.deliveryMethod = deliveryMethod;
        this.shippingName = shippingName;
        this.shippingPhone = shippingPhone;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.items = items;
    }

    // Getters only for brevity
    public long getId() { return id; }
    public double getSubtotal() { return subtotal; }
    public double getShippingFee() { return shippingFee; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    public String getDeliveryMethod() { return deliveryMethod; }
    public String getShippingName() { return shippingName; }
    public String getShippingPhone() { return shippingPhone; }
    public String getShippingAddress() { return shippingAddress; }
    public String getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return items; }

    public void setId(long id) {
        this.id = id;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
