package com.bms.controller;

import com.bms.dto.FDCreationRequest;
import com.bms.dto.FDCreationResponse;
import com.bms.dto.FDDetailsResponse;
import com.bms.service.FixedDepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;

@RestController
@RequestMapping("/api/fixed-deposits")
@Tag(name = "Fixed Deposit", description = "APIs for managing Fixed Deposits")
public class FixedDepositController {

    @Autowired
    private FixedDepositService fixedDepositService;

    @Operation(
        summary = "Create a new Fixed Deposit",
        description = "Creates a new Fixed Deposit with the specified amount and term"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Fixed Deposit created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or insufficient funds",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(hidden = true)
            )
        )
    })
    @PostMapping
    public ResponseEntity<FDCreationResponse> createFD(@Valid @RequestBody FDCreationRequest request) {
        FDCreationResponse response = fixedDepositService.createFD(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Get all Fixed Deposits for an account",
        description = "Retrieves all Fixed Deposits associated with the specified account"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fixed Deposits retrieved successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<FDDetailsResponse>> getFDsByAccount(
            @Parameter(description = "Account number", required = true)
            @PathVariable String accountNumber) {
        List<FDDetailsResponse> fds = fixedDepositService.getFDsByAccount(accountNumber);
        return new ResponseEntity<>(fds, HttpStatus.OK);
    }

    @Operation(
        summary = "Get Fixed Deposit details",
        description = "Retrieves details of a specific Fixed Deposit"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fixed Deposit details retrieved successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Fixed Deposit not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/{fdAccountNumber}")
    public ResponseEntity<FDDetailsResponse> getFDDetails(
            @Parameter(description = "FD account number", required = true)
            @PathVariable String fdAccountNumber) {
        FDDetailsResponse fd = fixedDepositService.getFDDetails(fdAccountNumber);
        return new ResponseEntity<>(fd, HttpStatus.OK);
    }
} 