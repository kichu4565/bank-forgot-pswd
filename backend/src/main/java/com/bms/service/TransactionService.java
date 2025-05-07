package com.bms.service;

import com.bms.dto.TransactionRequest;
import com.bms.dto.TransactionResponse;
import java.util.List;

public interface TransactionService {
    
    TransactionResponse processTransaction(TransactionRequest request);
    
    TransactionResponse getTransactionDetails(String transactionId);
    
    List<TransactionResponse> getTransactionHistory(String accountNumber);
} 