package com.bms.dto;

import com.bms.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Request object for creating a new bank account")
public class AccountCreationRequest {
    
    @Schema(description = "Full name of the account holder", example = "John Doe")
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Schema(description = "Date of birth of the account holder", example = "1990-01-01")
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @Schema(description = "Nationality of the account holder", example = "Indian")
    @NotBlank(message = "Nationality is required")
    private String nationality;
    
    @Schema(description = "Phone number of the account holder (10 digits)", example = "9876543210")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    
    @Schema(description = "Permanent address of the account holder", example = "123, Main Street, City, Country")
    @NotBlank(message = "Permanent address is required")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String permanentAddress;
    
    @Schema(description = "Type of government issued ID", example = "Aadhaar")
    @NotBlank(message = "Government issued ID type is required")
    private String governmentIssuedID;
    
    @Schema(description = "ID number of the government issued document", example = "1234-5678-9101")
    @NotBlank(message = "ID number is required")
    private String idNumber;
    
    @Schema(description = "Type of account to be created", example = "SAVINGS")
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    @Schema(description = "Initial deposit amount (minimum ₹500)", example = "1000.00")
    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "500.00", message = "Initial deposit must be at least ₹500")
    private BigDecimal initialDeposit;

    public void setInitialDeposit(Object value) {
        if (value instanceof Number) {
            this.initialDeposit = new BigDecimal(value.toString());
        } else if (value instanceof String) {
            this.initialDeposit = new BigDecimal((String) value);
        } else {
            throw new IllegalArgumentException("Initial deposit must be a number or string");
        }
    }
} 