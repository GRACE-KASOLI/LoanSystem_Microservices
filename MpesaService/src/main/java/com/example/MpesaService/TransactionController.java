package com.example.MpesaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")  // Base path
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/repay")
    public ResponseEntity<Transactions> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        System.out.println("Received request in MpesaService: " + transactionRequest);

        // Convert DTO to Transaction Entity
        Transactions transaction = new Transactions();
        transaction.setLoanId(transactionRequest.getLoanId());
        transaction.setPhoneNumber(transactionRequest.getPhoneNumber());

        // ✅ Ensure BigDecimal amount
        transaction.setAmount(transactionRequest.getAmount());

        // ✅ Convert String to TransactionType enum
        transaction.setType(TransactionType.valueOf(transactionRequest.getType().toUpperCase()));

        // Save Transaction
        Transactions savedTransaction = transactionService.saveTransaction(transaction);

        System.out.println("Saved transaction: " + savedTransaction);
        return ResponseEntity.ok(savedTransaction);
    }
}
