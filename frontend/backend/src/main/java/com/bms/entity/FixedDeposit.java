package com.bms.entity;

import com.bms.enums.FDStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fixed_deposits")
public class FixedDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fd_account_number", unique = true, nullable = false)
    private String fdAccountNumber;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "500.00", message = "FD amount must be at least ₹500")
    private BigDecimal principalAmount;

    @NotNull(message = "Interest rate is required")
    private BigDecimal interestRate;

    @NotNull(message = "Term period is required")
    @Min(value = 6, message = "FD duration must be at least 6 months")
    private Integer termMonths;

    @NotNull(message = "Maturity date is required")
    private LocalDateTime maturityDate;

    private BigDecimal interestEarned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FDStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = FDStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 