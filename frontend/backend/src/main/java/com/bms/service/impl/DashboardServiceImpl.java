package com.bms.service.impl;

import com.bms.dto.DashboardResponse;
import com.bms.entity.Account;
import com.bms.entity.Transaction;
import com.bms.exception.AccountNotFoundException;
import com.bms.repository.AccountRepository;
import com.bms.repository.TransactionRepository;
import com.bms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public DashboardResponse getAccountDashboard(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        List<Transaction> monthlyTransactions = transactionRepository
                .findBySourceAccountNumberOrDestinationAccountNumberOrderByTimestampDesc(accountNumber, accountNumber);

        BigDecimal monthlyDeposits = calculateMonthlyDeposits(monthlyTransactions, accountNumber);
        BigDecimal monthlyWithdrawals = calculateMonthlyWithdrawals(monthlyTransactions, accountNumber);
        int transactionCount = monthlyTransactions.size();

        List<DashboardResponse.TransactionSummary> recentTransactions = monthlyTransactions.stream()
                .limit(5)
                .map(tx -> new DashboardResponse.TransactionSummary(
                        tx.getTransactionId(),
                        tx.getSourceAccountNumber().equals(accountNumber) ? "DEBIT" : "CREDIT",
                        tx.getAmount(),
                        tx.getTimestamp(),
                        tx.getStatus()
                ))
                .collect(Collectors.toList());

        return new DashboardResponse(
                new DashboardResponse.AccountDetails(
                        account.getAccountNumber(),
                        account.getCustomer().getFullName(),
                        account.getBalance(),
                        account.getAccountType().name(),
                        "ACTIVE", // Default status since it's not stored in the entity
                        account.getCreatedAt()
                ),
                new DashboardResponse.TransactionSummary(
                        null, // No transaction ID for summary
                        null, // No type for summary
                        monthlyDeposits.subtract(monthlyWithdrawals), // Net amount
                        LocalDateTime.now(), // Current timestamp
                        "SUMMARY" // Status for summary
                ),
                recentTransactions
        );
    }

    private BigDecimal calculateMonthlyDeposits(List<Transaction> transactions, String accountNumber) {
        return transactions.stream()
                .filter(tx -> tx.getDestinationAccountNumber().equals(accountNumber))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlyWithdrawals(List<Transaction> transactions, String accountNumber) {
        return transactions.stream()
                .filter(tx -> tx.getSourceAccountNumber().equals(accountNumber))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 