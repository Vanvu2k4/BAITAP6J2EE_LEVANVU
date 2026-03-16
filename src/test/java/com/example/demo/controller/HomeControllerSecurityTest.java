package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;

import org.junit.jupiter.api.Test;

class HomeControllerSecurityTest {

    private final HomeController homeController = new HomeController();

    @Test
    void homeReturnsGreetingWithAuthenticatedUserName() {
        Principal principal = () -> "admin";

        String response = homeController.home(principal);

        assertEquals("Hello, admin", response);
    }
}
