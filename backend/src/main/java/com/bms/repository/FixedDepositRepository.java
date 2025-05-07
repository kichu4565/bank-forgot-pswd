package com.bms.repository;

import com.bms.entity.FixedDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long> {
    
    Optional<FixedDeposit> findByFdAccountNumber(String fdAccountNumber);
    
    List<FixedDeposit> findByAccount_AccountNumber(String accountNumber);
    
    List<FixedDeposit> findByAccount_AccountNumberAndStatus(String accountNumber, String status);
    
    List<FixedDeposit> findByMaturityDateLessThanAndStatus(LocalDateTime currentDate, String status);
} 