package com.bms.service.impl;

import com.bms.dto.LoginRequest;
import com.bms.dto.LoginResponse;
import com.bms.entity.Account;
import com.bms.exception.AuthenticationException;
import com.bms.repository.AccountRepository;
import com.bms.service.AuthService;
import com.bms.util.JwtUtil;
import com.bms.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordUtil passwordUtil;

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        // Find account by account number
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AuthenticationException("Invalid account number or password"));

        // Verify password
        if (!passwordUtil.verifyPassword(request.getPassword(), account.getPassword())) {
            throw new AuthenticationException("Invalid account number or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(account.getAccountNumber());
        LocalDateTime tokenExpiry = LocalDateTime.now().plusHours(1); // Token valid for 1 hour

        // Create and return response
        return new LoginResponse(
                "Login successful",
                token,
                tokenExpiry,
                account.getCustomer().getFullName(),
                account.getAccountNumber(),
                account.getBalance().doubleValue()
        );
    }
} 