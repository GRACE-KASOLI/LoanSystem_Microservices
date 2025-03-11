package com.example.MpesaService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    Optional<Transactions> findByLoanIdAndPhoneNumberAndAmountAndType(
            Long loanId, String phoneNumber, BigDecimal amount, TransactionType type
    );
}
