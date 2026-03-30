package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Tìm kiếm theo tên
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // Tìm kiếm theo tên (không phân trang)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Lọc theo category
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
    List<Product> findByCategoryId(Integer categoryId);

    // Tìm kiếm và lọc theo category
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> findByKeywordAndCategory(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId, Pageable pageable);
}