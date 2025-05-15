package com.example.gamingstore.model;

public class OrderItem {
    private long id;
    private String productName;
    private double productPrice;
    private String productColor;
    private int quantity;
    private String productImage; // <-- Thêm dòng này

    public OrderItem() {
        // default constructor
    }

    public OrderItem(long id, String productName, double productPrice, String productColor, int quantity, String productImage) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productColor = productColor;
        this.quantity = quantity;
        this.productImage = productImage;
    }

    // Getters
    public long getId() { return id; }
    public String getProductName() { return productName; }
    public double getProductPrice() { return productPrice; }
    public String getProductColor() { return productColor; }
    public int getQuantity() { return quantity; }
    public String getProductImage() { return productImage; } // <-- Getter mới

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductImage(String productImage) { // <-- Setter mới
        this.productImage = productImage;
    }
}
