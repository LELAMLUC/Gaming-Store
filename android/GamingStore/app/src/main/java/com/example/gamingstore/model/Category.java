package com.example.gamingstore.model;

public class Category {
    private Long id; // ID của category
    private String name; // Tên của category
    private String icon; // URL hoặc tên file của icon

    // Constructor, getters, và setters
    public Category(Long id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
