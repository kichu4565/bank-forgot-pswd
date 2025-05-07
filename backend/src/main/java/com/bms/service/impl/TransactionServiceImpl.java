package com.bms.service.impl;

import com.bms.dto.TransactionRequest;
import com.bms.dto.TransactionResponse;
import com.bms.entity.Transaction;
import com.bms.exception.InsufficientFundsException;
import com.bms.exception.ResourceNotFoundException;
import com.bms.repository.TransactionRepository;
import com.bms.service.AccountService;
import com.bms.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
    public TransactionResponse processTransaction(TransactionRequest request) {
        // Set source account number from authenticated user if not provided
        if (request.getSourceAccountNumber() == null || request.getSourceAccountNumber().isBlank()) {
            String loggedInAccountNumber = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            request.setSourceAccountNumber(loggedInAccountNumber);
        }
        // Prevent self-transfer
        if (request.getSourceAccountNumber().equals(request.getDestinationAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }
        // Validate source account exists and has sufficient funds
        BigDecimal sourceBalance = accountService.getAccountBalance(request.getSourceAccountNumber());
        if (sourceBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in source account");
        }

        // Validate destination account exists
        if (!accountService.accountExists(request.getDestinationAccountNumber())) {
            throw new ResourceNotFoundException("Destination account not found");
        }

        // Generate unique transaction ID
        String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Update account balances
        accountService.updateBalance(request.getSourceAccountNumber(), request.getAmount().negate());
        accountService.updateBalance(request.getDestinationAccountNumber(), request.getAmount());
        
        // Create transaction entity
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setSourceAccountNumber(request.getSourceAccountNumber());
        transaction.setDestinationAccountNumber(request.getDestinationAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus("COMPLETED");
        transaction.setSourceAccountBalance(sourceBalance.subtract(request.getAmount()));
        
        // Save transaction
        transaction = transactionRepository.save(transaction);
        
        // Convert to response
        return convertToResponse(transaction);
    }

    @Override
    public TransactionResponse getTransactionDetails(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        return convertToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(String accountNumber) {
        if (!accountService.accountExists(accountNumber)) {
            throw new ResourceNotFoundException("Account not found");
        }
        
        List<Transaction> transactions = transactionRepository.findBySourceAccountNumberOrderByTimestampDesc(accountNumber);
        return transactions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse convertToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(transaction.getTransactionId());
        response.setSourceAccountNumber(transaction.getSourceAccountNumber());
        response.setDestinationAccountNumber(transaction.getDestinationAccountNumber());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setStatus(transaction.getStatus());
        response.setTimestamp(transaction.getTimestamp());
        response.setSourceAccountBalance(transaction.getSourceAccountBalance());
        return response;
    }
} 