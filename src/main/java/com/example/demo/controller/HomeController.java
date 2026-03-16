package com.example.demo.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // truy cập /
    @GetMapping("/")
    public String index() {
        return "Trang index - vào /home để xem nội dung";
    }

    // truy cập /home (cần login)
    @GetMapping("/home")
    public String home(Principal principal) {

        if (principal == null) {
            return "Chưa đăng nhập";
        }

        return "Hello, " + principal.getName();
    }

}

