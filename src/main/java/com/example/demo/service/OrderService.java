package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    // Tạo đơn hàng từ giỏ hàng
    public Order createOrder(com.example.demo.model.Account account, Map<Integer, Integer> cart) {
        Order order = new Order();
        order.setAccount(account);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderDetail> orderDetails = new java.util.ArrayList<>();
        long totalPrice = 0;

        for (Integer productId : cart.keySet()) {
            Product product = productService.get(productId);
            if (product != null) {
                int quantity = cart.get(productId);

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQuantity(quantity);
                detail.setPrice(product.getPrice());

                orderDetails.add(detail);
                totalPrice += product.getPrice() * quantity;
            }
        }

        order.setOrderDetails(orderDetails);
        order.setTotalPrice(totalPrice);

        // Lưu order
        Order savedOrder = orderRepository.save(order);

        // Lưu order details
        for (OrderDetail detail : orderDetails) {
            orderDetailRepository.save(detail);
        }

        return savedOrder;
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

