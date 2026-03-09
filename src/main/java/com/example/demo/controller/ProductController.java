package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // LIST PRODUCT
    @GetMapping
    public String index(Model model) {
        model.addAttribute("listProduct", productService.getAll());
        return "product/products";
    }

    // FORM CREATE
    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());

        return "product/create";
    }

    // CREATE PRODUCT
    @PostMapping("/create")
    public String create(
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
}