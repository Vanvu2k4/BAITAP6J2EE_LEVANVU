package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private static final int ITEMS_PER_PAGE = 5;

    // Lấy tất cả sản phẩm
    @SuppressWarnings("unused")
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
                log.error("Error uploading image", e);
            }
        }
    }

    // ============ Helper method để tạo Pageable với Sort ============

    /**
     * Helper method để tạo Pageable object dựa vào sortBy parameter
     * @param page trang hiện tại (0-indexed)
     * @param sortBy loại sắp xếp (price_asc, price_desc, id)
     * @return Pageable object
     */
    private Pageable createPageable(int page, String sortBy) {
        if ("price_asc".equals(sortBy)) {
            return PageRequest.of(page, ITEMS_PER_PAGE, Sort.by("price").ascending());
        } else if ("price_desc".equals(sortBy)) {
            return PageRequest.of(page, ITEMS_PER_PAGE, Sort.by("price").descending());
        } else {
            return PageRequest.of(page, ITEMS_PER_PAGE, Sort.by("id").descending());
        }
    }

    // ============ CẤU HÌNH MỚI CHO TÌM KIẾM, PHÂN TRANG, SẮP XẾP ============

    // Phân trang
    public Page<Product> getPage(int page) {
        Pageable pageable = createPageable(page, "id");
        return productRepository.findAll(pageable);
    }

    // Tìm kiếm theo keyword
    @SuppressWarnings("unused")
    public Page<Product> searchByKeyword(String keyword, int page) {
        Pageable pageable = createPageable(page, "id");
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    // Lọc theo category
    @SuppressWarnings("unused")
    public Page<Product> filterByCategory(Integer categoryId, int page) {
        Pageable pageable = createPageable(page, "id");
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    // Tìm kiếm + Lọc theo category
    @SuppressWarnings("unused")
    public Page<Product> searchAndFilter(String keyword, Integer categoryId, int page) {
        Pageable pageable = createPageable(page, "id");
        return productRepository.findByKeywordAndCategory(keyword, categoryId, pageable);
    }

    // Sắp xếp theo giá tăng dần
    public Page<Product> sortByPriceAsc(int page) {
        Pageable pageable = createPageable(page, "price_asc");
        return productRepository.findAll(pageable);
    }

    // Sắp xếp theo giá giảm dần
    public Page<Product> sortByPriceDesc(int page) {
        Pageable pageable = createPageable(page, "price_desc");
        return productRepository.findAll(pageable);
    }

    // Tìm kiếm + Sắp xếp
    public Page<Product> searchWithSort(String keyword, String sortBy, int page) {
        Pageable pageable = createPageable(page, sortBy);
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    // Lọc + Sắp xếp
    public Page<Product> filterWithSort(Integer categoryId, String sortBy, int page) {
        Pageable pageable = createPageable(page, sortBy);
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    // Tìm kiếm + Lọc + Sắp xếp
    public Page<Product> searchFilterAndSort(String keyword, Integer categoryId, String sortBy, int page) {
        Pageable pageable = createPageable(page, sortBy);
        return productRepository.findByKeywordAndCategory(keyword, categoryId, pageable);
    }
}
