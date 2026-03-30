package com.example.demo.controller;

import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "account/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult result,
            Model model
    ) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Mat khau xac nhan khong khop");
        }

        if (result.hasErrors()) {
            return "account/register";
        }

        try {
            accountService.registerUser(form.getLoginName(), form.getUsername(), form.getEmail(), form.getPassword());
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registerError", ex.getMessage());
            return "account/register";
        }
    }

    @Data
    public static class RegisterForm {
        @NotBlank(message = "Login name khong duoc de trong")
        @Size(min = 4, max = 100, message = "Login name tu 4 den 100 ky tu")
        private String loginName;

        @NotBlank(message = "Ten hien thi khong duoc de trong")
        @Size(max = 255, message = "Ten hien thi toi da 255 ky tu")
        private String username;

        @NotBlank(message = "Email khong duoc de trong")
        @Email(message = "Email khong hop le")
        private String email;

        @NotBlank(message = "Mat khau khong duoc de trong")
        @Size(min = 6, max = 100, message = "Mat khau toi thieu 6 ky tu")
        private String password;

        @NotBlank(message = "Xac nhan mat khau khong duoc de trong")
        private String confirmPassword;
    }
}

