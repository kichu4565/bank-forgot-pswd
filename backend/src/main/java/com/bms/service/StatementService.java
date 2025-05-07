package com.bms.service;

import com.bms.dto.StatementRequest;
import com.bms.exception.AccountNotFoundException;
import com.bms.exception.InvalidDateRangeException;
import com.bms.exception.PDFGenerationException;
import java.io.ByteArrayOutputStream;

public interface StatementService {
    /**
     * Generates a PDF statement for the specified account and date range
     * @param accountNumber The account number
     * @param request The statement request containing date range
     * @return ByteArrayOutputStream containing the PDF data
     * @throws AccountNotFoundException if account is not found
     * @throws InvalidDateRangeException if date range is invalid
     * @throws PDFGenerationException if PDF generation fails
     */
    ByteArrayOutputStream generateStatement(String accountNumber, StatementRequest request) 
        throws AccountNotFoundException, InvalidDateRangeException, PDFGenerationException;
} 