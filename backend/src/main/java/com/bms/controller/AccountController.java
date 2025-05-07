package com.bms.controller;

import com.bms.dto.AccountCreationRequest;
import com.bms.dto.AccountCreationResponse;
import com.bms.dto.ErrorResponse;
import com.bms.service.AccountService;
import com.bms.entity.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(
        summary = "Create a new bank account",
        description = "Creates a new bank account with the provided customer details"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Account created successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @PostMapping("/create")
    public ResponseEntity<AccountCreationResponse> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        AccountCreationResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Get account details",
        description = "Fetches account details by account number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Account details fetched successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AccountCreationResponse.AccountDetails.class)
        )
    )
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountCreationResponse.AccountDetails> getAccountDetails(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        AccountCreationResponse.AccountDetails details = new AccountCreationResponse.AccountDetails(
            account.getAccountNumber(),
            account.getCustomer().getFullName(),
            account.getCustomer().getPhoneNumber(),
            account.getCustomer().getPermanentAddress(),
            account.getCustomer().getGovernmentIssuedID(),
            account.getCustomer().getIdNumber(),
            account.getAccountType().name(),
            account.getBalance().doubleValue(),
            account.getAccountCreationDate()
        );
        return ResponseEntity.ok(details);
    }
} 