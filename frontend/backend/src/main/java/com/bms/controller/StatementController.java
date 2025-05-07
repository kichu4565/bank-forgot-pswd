package com.bms.controller;

import com.bms.dto.StatementRequest;
import com.bms.exception.AccountNotFoundException;
import com.bms.exception.InvalidDateRangeException;
import com.bms.exception.PDFGenerationException;
import com.bms.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/statements")
@Tag(name = "Statement", description = "APIs for account statements")
public class StatementController {

    @Autowired
    private StatementService statementService;

    @Operation(
        summary = "Download account statement",
        description = "Generates and downloads a PDF statement for the specified account and date range"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Statement generated successfully",
            content = @Content(mediaType = "application/pdf")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid date range",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "PDF generation failed",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(hidden = true)
            )
        )
    })
    @PostMapping(value = "/{accountNumber}/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<ByteArrayResource> downloadStatement(
            @Parameter(description = "Account number", required = true)
            @PathVariable String accountNumber,
            @Valid @RequestBody StatementRequest request) 
            throws AccountNotFoundException, InvalidDateRangeException, PDFGenerationException {
        
        ByteArrayOutputStream pdfStream = statementService.generateStatement(accountNumber, request);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=statement_" + accountNumber + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdfStream.toByteArray()));
    }
} 