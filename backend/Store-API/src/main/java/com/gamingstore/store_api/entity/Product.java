package com.gamingstore.store_api.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // tự động tăng ID
    private Long id;

    private String name;

    private String imageUrl; // Đây có thể là ảnh chính của sản phẩm

    private double price;

    private double rating;

    private int discountPercent;

    private int categoryId;

    @ElementCollection // Sử dụng @ElementCollection để lưu trữ danh sách URL hình ảnh
    private List<String> imageUrls; // Danh sách các URL ảnh của sản phẩm

    private String description;

    // Constructor không tham số để JPA có thể khởi tạo entity
    public Product() {
    }

    // Constructor với tất cả các thuộc tính
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

    // Getter và Setter cho các thuộc tính
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}