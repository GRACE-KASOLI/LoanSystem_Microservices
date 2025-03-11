package com.example.LoanService;

import com.example.LoanService.RepaymentRepository;
import com.example.LoanService.Repayment;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long> {
    List<Repayment> findByLoanId(Long loanId);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Repayment r")
    BigDecimal getTotalRepaidAmount();
}

