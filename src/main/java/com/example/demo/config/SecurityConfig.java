package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                        .requestMatchers("/403", "/register").permitAll()
                        .requestMatchers("/products/add", "/products/add/**").hasRole("ADMIN")
                        .requestMatchers("/products/edit", "/products/edit/**").hasRole("ADMIN")
                        .requestMatchers("/products/delete", "/products/delete/**").hasRole("ADMIN")
                        .requestMatchers("/orders", "/orders/{id}", "/orders/cancel/**").hasRole("ADMIN")
                        .requestMatchers("/cart/**", "/orders/checkout").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/products", "/products/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/products", true)
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
                .logout(logout -> logout.permitAll());

        return http.build();
    }

}