package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final List<Category> list = new ArrayList<>();

    public CategoryService() {
        list.add(new Category(1, "Điện thoại"));
        list.add(new Category(2, "Laptop"));
    }

    public List<Category> getAll() {
        return list;
    }

    public Category get(int id) {
        return list.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}