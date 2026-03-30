package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService;

    // VIEW CART
    @GetMapping
    public String viewCart(HttpSession session, Model model) {

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        Map<Product, Integer> cartDetails = new HashMap<>();
        long totalPrice = 0;

        if (cart != null) {
            for (Integer productId : cart.keySet()) {
                Product product = productService.get(productId);
                if (product != null) {
                    int quantity = cart.get(productId);
                    cartDetails.put(product, quantity);
                    totalPrice += product.getPrice() * quantity;
                }
            }
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartCount", cart != null ? cart.size() : 0);

        return "cart/cart";
    }

    // UPDATE QUANTITY
    @PostMapping("/update-quantity/{productId}")
    public String updateQuantity(
            @PathVariable int productId,
            @RequestParam int quantity,
            HttpSession session) {

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart != null && quantity > 0) {
            cart.put(productId, quantity);
        }

        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    // REMOVE FROM CART
    @GetMapping("/remove/{productId}")
    public String removeFromCart(
            @PathVariable int productId,
            HttpSession session) {

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart != null) {
            cart.remove(productId);
        }

        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    // CLEAR CART
    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }
}

