package com.example.gamingstore.model;

import java.util.List;

public class Product {
    private Long id;
    private String name;
    private String imageUrl;
    private double price;
    private double rating;
    private int discountPercent;
    private int categoryId;
    private List<String> imageUrls;
    private String description;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    private int quantity; // ✅ Thêm số lượng sản phẩm

    private String colors; // ✅ Thêm trường colors để lưu các mã màu

    private int soldQuantity;   // ✅ Số lượng đã bán
    // Constructor đầy đủ
    public Product(String name, String imageUrl, double price, double rating, int discountPercent, int categoryId, List<String> imageUrls, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
        this.discountPercent = discountPercent;
        this.categoryId = categoryId;
        this.imageUrls = imageUrls;
        this.description = description;
    }

    // Constructor đơn giản (tuỳ chọn, dùng khi test dữ liệu cứng)
    public Product(String name, String imageUrl, double price, double rating, int discountPercent, int categoryId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
        this.discountPercent = discountPercent;
        this.categoryId = categoryId;
    }
    public Product(String name, String imageUrl, double price, double rating) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
    }

    // Getters và Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
