package com.bms.service;

import com.bms.dto.AccountCreationRequest;
import com.bms.dto.AccountCreationResponse;
import com.bms.exception.ResourceNotFoundException;
import com.bms.entity.Account;

import java.math.BigDecimal;

public interface AccountService {
    AccountCreationResponse createAccount(AccountCreationRequest request);
    
    BigDecimal getAccountBalance(String accountNumber) throws ResourceNotFoundException;
    
    boolean accountExists(String accountNumber);
    
    void updateBalance(String accountNumber, BigDecimal amount) throws ResourceNotFoundException;
    
    Account getAccountByAccountNumber(String accountNumber) throws ResourceNotFoundException;
} 