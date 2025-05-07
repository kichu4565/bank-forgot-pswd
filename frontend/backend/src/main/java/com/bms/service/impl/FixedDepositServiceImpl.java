package com.bms.service.impl;

import com.bms.dto.FDCreationRequest;
import com.bms.dto.FDCreationResponse;
import com.bms.dto.FDDetailsResponse;
import com.bms.entity.Account;
import com.bms.entity.FixedDeposit;
import com.bms.enums.FDStatus;
import com.bms.exception.InsufficientFundsException;
import com.bms.repository.AccountRepository;
import com.bms.repository.FixedDepositRepository;
import com.bms.service.FixedDepositService;
import com.bms.util.FDNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FixedDepositServiceImpl implements FixedDepositService {

    @Autowired
    private FixedDepositRepository fdRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FDNumberGenerator fdNumberGenerator;

    private static final BigDecimal MIN_FD_AMOUNT = new BigDecimal("500.00");
    private static final BigDecimal INTEREST_RATE = new BigDecimal("6.5"); // 6.5% annual interest

    @Override
    @Transactional
    public FDCreationResponse createFD(FDCreationRequest request) {
        // Validate account
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Validate balance
        if (account.getBalance().compareTo(request.getPrincipalAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds to create Fixed Deposit");
        }

        // Create FD
        FixedDeposit fd = new FixedDeposit();
        fd.setFdAccountNumber(fdNumberGenerator.generateFDNumber());
        fd.setAccount(account);
        fd.setPrincipalAmount(request.getPrincipalAmount());
        fd.setInterestRate(INTEREST_RATE);
        fd.setTermMonths(request.getTermMonths());
        fd.setMaturityDate(LocalDateTime.now().plusMonths(request.getTermMonths()));

        // Deduct amount from account
        account.setBalance(account.getBalance().subtract(request.getPrincipalAmount()));
        accountRepository.save(account);

        // Save FD
        fd = fdRepository.save(fd);

        // Calculate expected maturity amount
        BigDecimal expectedMaturityAmount = calculateMaturityAmount(
            request.getPrincipalAmount(),
            INTEREST_RATE,
            request.getTermMonths()
        );

        // Prepare response
        FDCreationResponse.FDDetails details = new FDCreationResponse.FDDetails();
        details.setFdAccountNumber(fd.getFdAccountNumber());
        details.setPrincipalAmount(fd.getPrincipalAmount());
        details.setInterestRate(fd.getInterestRate());
        details.setTermMonths(fd.getTermMonths());
        details.setMaturityDate(fd.getMaturityDate());
        details.setExpectedMaturityAmount(expectedMaturityAmount);

        return new FDCreationResponse("Fixed Deposit created successfully", details);
    }

    @Override
    public List<FDDetailsResponse> getFDsByAccount(String accountNumber) {
        List<FixedDeposit> fds = fdRepository.findByAccount_AccountNumber(accountNumber);
        return fds.stream()
                .map(this::convertToFDDetailsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FDDetailsResponse getFDDetails(String fdAccountNumber) {
        FixedDeposit fd = fdRepository.findByFdAccountNumber(fdAccountNumber)
                .orElseThrow(() -> new RuntimeException("Fixed Deposit not found"));
        return convertToFDDetailsResponse(fd);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    @Transactional
    public void processMaturedFDs() {
        LocalDateTime now = LocalDateTime.now();
        List<FixedDeposit> maturedFDs = fdRepository.findByMaturityDateLessThanAndStatus(now, FDStatus.ACTIVE.name());

        for (FixedDeposit fd : maturedFDs) {
            // Calculate final interest
            BigDecimal interestEarned = calculateInterest(
                fd.getPrincipalAmount(),
                fd.getInterestRate(),
                fd.getTermMonths()
            );
            fd.setInterestEarned(interestEarned);
            fd.setStatus(FDStatus.MATURED);

            // Add maturity amount to account
            Account account = fd.getAccount();
            BigDecimal maturityAmount = fd.getPrincipalAmount().add(interestEarned);
            account.setBalance(account.getBalance().add(maturityAmount));
            accountRepository.save(account);

            fdRepository.save(fd);
        }
    }

    private FDDetailsResponse convertToFDDetailsResponse(FixedDeposit fd) {
        FDDetailsResponse response = new FDDetailsResponse();
        response.setFdAccountNumber(fd.getFdAccountNumber());
        response.setPrincipalAmount(fd.getPrincipalAmount());
        response.setInterestRate(fd.getInterestRate());
        response.setTermMonths(fd.getTermMonths());
        response.setMaturityDate(fd.getMaturityDate());
        response.setInterestEarned(fd.getInterestEarned());
        response.setStatus(fd.getStatus());
        response.setCreatedAt(fd.getCreatedAt());
        return response;
    }

    private BigDecimal calculateMaturityAmount(BigDecimal principal, BigDecimal rate, int months) {
        BigDecimal interest = calculateInterest(principal, rate, months);
        return principal.add(interest);
    }

    private BigDecimal calculateInterest(BigDecimal principal, BigDecimal rate, int months) {
        // Simple interest calculation: P * R * T
        // P = Principal, R = Rate per annum, T = Time in years
        BigDecimal timeInYears = new BigDecimal(months).divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP);
        return principal.multiply(rate)
                .multiply(timeInYears)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
} 