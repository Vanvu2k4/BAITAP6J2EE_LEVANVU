package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // LIST PRODUCT - WITH SEARCH, FILTER, SORT, PAGINATION
    @GetMapping
    public String index(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            Model model) {

        Page<Product> products;

        // Xử lý các trường hợp khác nhau
        if (!keyword.isEmpty() && categoryId != null && categoryId > 0) {
            // Tìm kiếm + Lọc + Sắp xếp
            products = productService.searchFilterAndSort(keyword, categoryId, sortBy, page);
        } else if (!keyword.isEmpty()) {
            // Tìm kiếm + Sắp xếp
            products = productService.searchWithSort(keyword, sortBy, page);
        } else if (categoryId != null && categoryId > 0) {
            // Lọc + Sắp xếp
            products = productService.filterWithSort(categoryId, sortBy, page);
        } else if ("price_asc".equals(sortBy)) {
            // Sắp xếp tăng dần
            products = productService.sortByPriceAsc(page);
        } else if ("price_desc".equals(sortBy)) {
            // Sắp xếp giảm dần
            products = productService.sortByPriceDesc(page);
        } else {
            // Mặc định
            products = productService.getPage(page);
        }

        model.addAttribute("listProduct", products.getContent());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("hasNext", products.hasNext());
        model.addAttribute("hasPrevious", products.hasPrevious());

        return "product/products";
    }

    // FORM ADD
    @GetMapping("/add")
    public String addForm(Model model) {

        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());

        return "product/create";
    }

    // ADD PRODUCT
    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute Product product,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam("categoryId") int categoryId,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        product.setCategory(categoryService.get(categoryId));

        productService.uploadImage(product, file);

        productService.add(product);

        return "redirect:/products";
    }

    // FORM EDIT
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {

        model.addAttribute("product", productService.get(id));
        model.addAttribute("categories", categoryService.getAll());

        return "product/edit";
    }

    // UPDATE PRODUCT
    @PostMapping("/edit")
    public String edit(
            @Valid @ModelAttribute Product product,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam("categoryId") int categoryId,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        product.setCategory(categoryService.get(categoryId));

        productService.uploadImage(product, file);

        productService.update(product);

        return "redirect:/products";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {

        productService.delete(id);

        return "redirect:/products";
    }

    // ADD TO CART
    @GetMapping("/add-to-cart/{id}")
    public String addToCart(
            @PathVariable int id,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            HttpSession session) {

        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        // Nếu sản phẩm đã trong giỏ, tăng số lượng
        if (cart.containsKey(id)) {
            cart.put(id, cart.get(id) + quantity);
        } else {
            cart.put(id, quantity);
        }

        session.setAttribute("cart", cart);

        return "redirect:/products";
    }
}