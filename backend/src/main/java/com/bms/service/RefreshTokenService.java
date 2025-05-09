package com.bms.service;

import com.bms.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String accountNumber);
    boolean validateRefreshToken(String token, String accountNumber);
    void deleteByToken(String token);
    void deleteByAccountNumber(String accountNumber);
} 