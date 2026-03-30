package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String checkout(HttpSession session, Model model) {

        // Lấy thông tin người dùng từ session
        String username = (String) session.getAttribute("username");

        if (username == null || username.isEmpty()) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        // Lấy giỏ hàng
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }

        // Lấy tài khoản theo loginName
        Account account = accountService.findByLoginName(username);
        if (account == null) {
            return "redirect:/login";
        }

        // Tạo đơn hàng
        com.example.demo.model.Order order = orderService.createOrder(account, cart);

        // Xóa giỏ hàng
        session.removeAttribute("cart");

        model.addAttribute("order", order);
        model.addAttribute("message", "Đặt hàng thành công! Mã đơn hàng: #" + order.getId());

        return "order/checkout-success";
    }

    // CANCEL ORDER
    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable int id) {
        orderService.updateStatus(id, "CANCELLED");
        return "redirect:/orders";
    }
}


