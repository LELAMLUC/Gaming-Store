package com.gamingstore.store_api.controller;

import com.gamingstore.store_api.entity.Category;
import com.gamingstore.store_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/takeAll")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
