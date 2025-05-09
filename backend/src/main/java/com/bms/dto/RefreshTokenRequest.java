package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @Schema(description = "Refresh token", example = "randomlyGeneratedRefreshTokenHere")
    private String refreshToken;

    @Schema(description = "Account number", example = "BANK123456")
    private String accountNumber;
} 