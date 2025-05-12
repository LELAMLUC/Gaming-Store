package com.gamingstore.store_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private String color;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Cart() {}

    public Cart(int quantity, String color, Account account, Product product) {
        this.quantity = quantity;
        this.color = color;
        this.account = account;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getColor() {
        return color;
    }

    public Account getAccount() {
        return account;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
