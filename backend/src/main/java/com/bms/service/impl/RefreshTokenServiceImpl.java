package com.bms.service.impl;

import com.bms.entity.RefreshToken;
import com.bms.repository.RefreshTokenRepository;
import com.bms.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60; // 7 days

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(String accountNumber) {
        String token = generateRandomToken();
        Instant expiryDate = Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setAccountNumber(accountNumber);
        refreshToken.setExpiryDate(expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean validateRefreshToken(String token, String accountNumber) {
        Optional<RefreshToken> opt = refreshTokenRepository.findByToken(token);
        return opt.isPresent() &&
               opt.get().getAccountNumber().equals(accountNumber) &&
               opt.get().getExpiryDate().isAfter(Instant.now());
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        refreshTokenRepository.deleteByAccountNumber(accountNumber);
    }

    private String generateRandomToken() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
} 