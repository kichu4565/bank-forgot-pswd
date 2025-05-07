package com.bms.service;

import com.bms.dto.LoginRequest;
import com.bms.dto.LoginResponse;

public interface AuthService {
    LoginResponse authenticate(LoginRequest request);
} 