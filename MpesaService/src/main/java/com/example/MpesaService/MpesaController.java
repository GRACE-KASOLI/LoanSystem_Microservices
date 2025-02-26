package com.example.MpesaService;

import com.example.MpesaService.Transaction;
import com.example.MpesaService.TransactionStatus;
import com.example.MpesaService.TransactionType;
import com.example.MpesaService.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mpesa")
public class MpesaController {

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/disburse")
    public ResponseEntity<String> disburseLoan(@RequestParam Long loanId,
                                               @RequestParam String phoneNumber,
                                               @RequestParam String amount) {
        System.out.println("Received disbursement request: Loan ID - " + loanId
                + ", Phone - " + phoneNumber + ", Amount - " + amount);

        // Save the transaction with the provided loanId
        saveTransaction(loanId, phoneNumber, amount, TransactionType.DISBURSEMENT);

        return ResponseEntity.ok("Loan of " + amount + " has been disbursed to " + phoneNumber);
    }

    @PostMapping("/pay")
    public ResponseEntity<String> processRepayment(@RequestParam Long loanId,
                                                   @RequestParam String phoneNumber,
                                                   @RequestParam String amount) {
        System.out.println("Received payment request: Loan ID - " + loanId
                + ", Phone - " + phoneNumber + ", Amount - " + amount);

        // Save the transaction with the provided loanId
        saveTransaction(loanId, phoneNumber, amount, TransactionType.REPAYMENT);

        return ResponseEntity.ok("Payment of " + amount + " received from " + phoneNumber);
    }

    // New function to save transactions, using the provided loanId
    private void saveTransaction(Long loanId, String phoneNumber, String amount, TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setLoanId(loanId);  // Now using the loan ID from the loans table
        transaction.setPhoneNumber(phoneNumber);
        transaction.setAmount(Double.parseDouble(amount));
        transaction.setType(type);
        transaction.setStatus(TransactionStatus.SUCCESS);  // Or set as PENDING if that fits your logic
        transactionRepository.save(transaction);

        System.out.println("Transaction saved: " + type + " - " + amount
                + " for Loan ID " + loanId + " and Phone " + phoneNumber);
    }
}
