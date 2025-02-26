package com.example.MpesaService;

import com.example.MpesaService.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //Transaction findByTransactionId(String transactionId);
}

