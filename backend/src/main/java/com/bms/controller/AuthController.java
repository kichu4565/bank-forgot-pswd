package com.bms.controller;

import com.bms.dto.LoginRequest;
import com.bms.dto.LoginResponse;
import com.bms.dto.LogoutResponse;
import com.bms.dto.TokenRefreshResponse;
import com.bms.service.AuthService;
import com.bms.util.JwtUtil;
import com.bms.service.RefreshTokenService;
import com.bms.entity.RefreshToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Operation(
        summary = "Login to the system",
        description = "Authenticate user with account number and password"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Login successful",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary = "Logout from the system",
        description = "Invalidate the current session and clear security context"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Logout successful",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody(required = false) String refreshToken) {
        SecurityContextHolder.clearContext();
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.deleteByToken(refreshToken);
        }
        return new ResponseEntity<>(
            new LogoutResponse("Logout successful"),
            HttpStatus.OK
        );
    }

    @Operation(
        summary = "Refresh authentication token",
        description = "Generate a new JWT token using the current token"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Token refreshed successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String accountNumber = request.getAccountNumber();
        if (refreshTokenService.validateRefreshToken(refreshToken, accountNumber)) {
            String newToken = jwtUtil.generateToken(accountNumber);
            LocalDateTime tokenExpiry = LocalDateTime.now().plusHours(1);
            return new ResponseEntity<>(
                new TokenRefreshResponse("Token refreshed successfully", newToken, tokenExpiry),
                HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
            new TokenRefreshResponse("Invalid or expired refresh token", null, null),
            HttpStatus.UNAUTHORIZED
        );
    }
} 