package com.bms.repository;

import com.bms.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findBySourceAccountNumber(String accountNumber);
    
    List<Transaction> findByDestinationAccountNumber(String accountNumber);
    
    List<Transaction> findBySourceAccountNumberOrDestinationAccountNumber(String sourceAccount, String destinationAccount);
    
    List<Transaction> findBySourceAccountNumberAndTimestampBetweenOrDestinationAccountNumberAndTimestampBetween(
            String sourceAccount, LocalDateTime startDate1, LocalDateTime endDate1,
            String destinationAccount, LocalDateTime startDate2, LocalDateTime endDate2);
    
    Transaction findByTransactionId(String transactionId);
    
    List<Transaction> findBySourceAccountNumberOrDestinationAccountNumberOrderByTimestampDesc(
            String sourceAccountNumber, String destinationAccountNumber);
            
    List<Transaction> findBySourceAccountNumberOrderByTimestampDesc(String accountNumber);
    
    List<Transaction> findByDestinationAccountNumberOrderByTimestampDesc(String accountNumber);

    List<Transaction> findBySourceAccountNumberOrDestinationAccountNumberAndTimestampBetween(
        String accountNumber1, String accountNumber2, LocalDateTime start, LocalDateTime end);
} 