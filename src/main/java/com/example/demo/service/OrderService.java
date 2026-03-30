package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    // Tạo đơn hàng từ giỏ hàng
    @Transactional
    public Order createOrder(Account account, Map<Integer, Integer> cart) {
        if (account == null) {
            throw new IllegalArgumentException("Tai khoan khong hop le");
        }
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Gio hang rong");
        }

        Order order = new Order();
        order.setAccount(account);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        long totalPrice = 0L;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Product product = productService.get(entry.getKey());
            Integer quantity = entry.getValue();

            if (product == null || quantity == null || quantity <= 0) {
                continue;
            }

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setPrice(product.getPrice());

            order.addOrderDetail(detail);
            totalPrice += product.getPrice() * quantity;
        }

        if (order.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Khong co san pham hop le de dat hang");
        }

        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo id
    public Order get(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Lấy đơn hàng của tài khoản
    public List<Order> getOrdersByAccount(Integer accountId) {
        return orderRepository.findByAccountId(accountId);
    }

    // Cập nhật trạng thái đơn hàng
    public void updateStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    // Xóa đơn hàng
    public void delete(int id) {
        orderRepository.deleteById(id);
    }
}
