package com.gamingstore.store_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    // Constructors
    public Wishlist() {}

    public Wishlist(Account account, Product product) {
        this.account = account;
        this.product = product;
    }

    // Getters and setters
    public Long getId() {
        return id;
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

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
