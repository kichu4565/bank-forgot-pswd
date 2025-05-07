package com.bms.controller;

import com.bms.dto.TransactionRequest;
import com.bms.dto.TransactionResponse;
import com.bms.dto.ErrorResponse;
import com.bms.service.TransactionService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Management", description = "APIs for managing financial transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(
        summary = "Process a new transaction",
        description = "Transfer money from one account to another. The transaction will only be completed if the source account has sufficient funds and both accounts exist."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Transaction processed successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request or insufficient funds",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Source or destination account not found",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @PostMapping
    public ResponseEntity<TransactionResponse> processTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.processTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.processTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary = "Get transaction details",
        description = "Retrieve details of a specific transaction"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Transaction details retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionDetails(@PathVariable String transactionId) {
        TransactionResponse response = transactionService.getTransactionDetails(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary = "Get transaction history",
        description = "Retrieve transaction history for an account"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Transaction history retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable String accountNumber) {
        List<TransactionResponse> response = transactionService.getTransactionHistory(accountNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
} 