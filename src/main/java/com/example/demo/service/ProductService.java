package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ProductService {

    private final List<Product> list = new ArrayList<>();

    public List<Product> getAll() {
        return list;
    }

    public Product get(int id) {
        return list.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void add(Product product) {
        int maxId = list.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0);

        product.setId(maxId + 1);
        list.add(product);
    }

    public void update(Product product) {
        Product old = get(product.getId());
        if (old != null) {
            old.setName(product.getName());
            old.setPrice(product.getPrice());
            old.setCategory(product.getCategory());
            if (product.getImage() != null) {
                old.setImage(product.getImage());
            }
        }
    }

    public void delete(int id) {
        list.removeIf(p -> p.getId() == id);
    }

    // Upload image
    public void uploadImage(Product product, MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImage(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
