package com.bms.dto;

import com.bms.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing account creation details")
public class AccountCreationResponse {
    
    @Schema(description = "Success message")
    private String message;
    
    @Schema(description = "Generated password for the account")
    private String generatedPassword;
    
    @Schema(description = "Account details")
    private AccountDetails accountDetails;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Account details")
    public static class AccountDetails {
        @Schema(description = "Account number", example = "BANK1CB0BD")
        private String accountNumber;
        
        @Schema(description = "Account holder's name", example = "John")
        private String accountHolderName;
        
        @Schema(description = "Phone number", example = "9876543234")
        private String phoneNumber;
        
        @Schema(description = "Permanent address", example = "123, Main Street, City, Country")
        private String permanentAddress;
        
        @Schema(description = "Government issued ID type", example = "Aadhaar")
        private String governmentIssuedID;
        
        @Schema(description = "ID number", example = "1234-5678-91111111")
        private String idNumber;
        
        @Schema(description = "Type of account", example = "SAVINGS")
        private String accountType;
        
        @Schema(description = "Initial balance", example = "1000")
        private double balance;
        
        @Schema(description = "Account creation date and time")
        private LocalDateTime accountCreationDate;
    }
} 