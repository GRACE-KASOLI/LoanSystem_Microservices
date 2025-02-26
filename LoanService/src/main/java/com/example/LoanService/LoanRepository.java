package com.example.LoanService;

import com.example.LoanService.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    @Query("SELECT l.id FROM Loan l WHERE l.phoneNumber = :phoneNumber AND l.status = 'DISBURSED'")
    Optional<Long> findActiveLoanIdByphoneNumber(String phoneNumber);
}

