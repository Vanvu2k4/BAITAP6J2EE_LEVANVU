package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.model.Order;
import com.example.demo.service.AccountService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AccountService accountService;

    // VIEW ALL ORDERS (Admin)
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAll());
        return "order/list";
    }

    // VIEW ORDER DETAILS
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable int id, Model model) {
        model.addAttribute("order", orderService.get(id));
        return "order/detail";
    }

    // CHECKOUT
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        Account account = accountService.findByLoginName(principal.getName());
        if (account == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.createOrder(account, cart);
            session.removeAttribute("cart");
            model.addAttribute("order", order);
            model.addAttribute("message", "Đặt hàng thành công! Mã đơn hàng: #" + order.getId());
            return "order/checkout-success";
        } catch (IllegalArgumentException ex) {
            return "redirect:/cart";
        }
    }

    // CANCEL ORDER
    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable int id) {
        orderService.updateStatus(id, "CANCELLED");
        return "redirect:/orders";
    }
}
