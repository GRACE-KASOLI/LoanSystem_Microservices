package com.example.LoanService;

import com.example.LoanService.RepaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "repayments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus paymentStatus = RepaymentStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
}
