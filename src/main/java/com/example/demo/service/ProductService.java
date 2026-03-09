package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo id
    public Product get(int id) {
        return productRepository.findById(id).orElse(null);
    }

    // Thêm sản phẩm
    public void add(Product product) {
        productRepository.save(product);
    }

    // Update
    public void update(Product product) {
        productRepository.save(product);
    }

    // Delete
    public void delete(int id) {
        productRepository.deleteById(id);
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