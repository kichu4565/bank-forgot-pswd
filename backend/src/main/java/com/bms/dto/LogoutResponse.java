package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for logout operation")
public class LogoutResponse {
    
    @Schema(description = "Success message", example = "Logout successful")
    private String message;
} 