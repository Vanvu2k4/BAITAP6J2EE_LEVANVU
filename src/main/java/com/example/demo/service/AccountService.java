package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Account account = accountRepository
                .findByLoginName(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new User(
                account.getLoginName(),
                account.getPassword(),
                account.getRoles()
                        .stream()
                        .map(role -> {
                            String roleName = role.getName();
                            String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
                            return new SimpleGrantedAuthority(authority);
                        })
                        .collect(Collectors.toList())
        );
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }

    public Account findByLoginName(String loginName) {
        return accountRepository.findByLoginName(loginName).orElse(null);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public Account registerUser(String loginName, String username, String email, String rawPassword) {
        String normalizedLoginName = loginName == null ? "" : loginName.trim();
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();

        if (normalizedLoginName.isEmpty() || normalizedEmail.isEmpty() || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Thong tin dang ky khong hop le");
        }

        if (accountRepository.existsByLoginName(normalizedLoginName)) {
            throw new IllegalArgumentException("Login name da ton tai");
        }

        if (accountRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email da ton tai");
        }

        Account account = new Account();
        account.setLoginName(normalizedLoginName);
        account.setUsername(username == null ? "" : username.trim());
        account.setEmail(normalizedEmail);
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.getRoles().add(getOrCreateRole("ROLE_USER"));

        return accountRepository.save(account);
    }

    @Transactional
    public void createAdminIfNotExists(String loginName, String username, String email, String rawPassword) {
        String normalizedLoginName = loginName == null ? "" : loginName.trim();
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();

        if (normalizedLoginName.isEmpty() || normalizedEmail.isEmpty()) {
            return;
        }

        if (accountRepository.existsByLoginName(normalizedLoginName) || accountRepository.existsByEmail(normalizedEmail)) {
            return;
        }

        Account admin = new Account();
        admin.setLoginName(normalizedLoginName);
        admin.setUsername(username == null ? "" : username.trim());
        admin.setEmail(normalizedEmail);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.getRoles().add(getOrCreateRole("ROLE_ADMIN"));
        admin.getRoles().add(getOrCreateRole("ROLE_USER"));

        accountRepository.save(admin);
    }

    @Transactional
    public Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }
}