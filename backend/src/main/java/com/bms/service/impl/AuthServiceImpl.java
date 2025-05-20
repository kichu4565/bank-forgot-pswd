package com.bms.service.impl;

import com.bms.dto.LoginRequest;
import com.bms.dto.LoginResponse;
import com.bms.entity.Account;
import com.bms.exception.AuthenticationException;
import com.bms.repository.AccountRepository;
import com.bms.service.AuthService;
import com.bms.util.JwtUtil;
import com.bms.util.PasswordUtil;
import com.bms.service.RefreshTokenService;
import com.bms.entity.RefreshToken;
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

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public boolean resetPassword(String accountNumber, String newPassword) {
        return accountRepository.findByAccountNumber(accountNumber).map(account -> {
            //  Check if new password matches the current one
            if (passwordUtil.verifyPassword(newPassword, account.getPassword())) {
                return false; // Do not allow reuse of old password
            }

            // ✅ Hash and save the new password
            String hashedPassword = passwordUtil.hashPassword(newPassword);
            account.setPassword(hashedPassword);
            accountRepository.save(account);
            return true;
        }).orElse(false); // account not found
    }

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

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(account.getAccountNumber());

        // Create and return response
        return new LoginResponse(
                "Login successful",
                token,
                tokenExpiry,
                account.getCustomer().getFullName(),
                account.getAccountNumber(),
                account.getBalance().doubleValue(),
                refreshToken.getToken(),
                refreshToken.getExpiryDate().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}
