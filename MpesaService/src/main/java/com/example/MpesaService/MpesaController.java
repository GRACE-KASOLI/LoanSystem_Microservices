package com.example.MpesaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/mpesa")
public class MpesaController {

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping(value = "/disburse", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> disburseLoan(@RequestBody MpesaPaymentRequest request) {
        System.out.println("Received disbursement request: Loan ID - " + request.getLoanId()
                + ", Phone - " + request.getPhoneNumber() + ", Amount - " + request.getAmount());

        // Save the transaction
        saveTransaction(request.getLoanId(), request.getPhoneNumber(), new BigDecimal(request.getAmount()), TransactionType.DISBURSEMENT);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("{\"message\": \"Loan of " + request.getAmount() + " has been disbursed to " + request.getPhoneNumber() + "\"}");
    }

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> processRepayment(@RequestBody MpesaPaymentRequest request) {
        System.out.println("Received payment request: Loan ID - " + request.getLoanId()
                + ", Phone - " + request.getPhoneNumber() + ", Amount - " + request.getAmount());

        // ✅ Convert amount to BigDecimal before passing to saveTransaction()
        saveTransaction(request.getLoanId(), request.getPhoneNumber(), new BigDecimal(request.getAmount()), TransactionType.REPAYMENT);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("{\"message\": \"Payment of " + request.getAmount() + " received from " + request.getPhoneNumber() + "\"}");
    }

    private void saveTransaction(Long loanId, String phoneNumber, BigDecimal amount, TransactionType type) {
        try {
            System.out.println("📌 Checking for duplicate transactions...");
            Optional<Transactions> existingTransaction = transactionRepository.findByLoanIdAndPhoneNumberAndAmountAndType(
                    loanId, phoneNumber, amount, type
            );

            if (existingTransaction.isPresent()) {
                System.out.println("🚨 Duplicate transaction detected: " + type + " - " + amount);
                return;
            }

            Transactions transaction = new Transactions();
            transaction.setLoanId(loanId);
            transaction.setPhoneNumber(phoneNumber);
            transaction.setAmount(amount);
            transaction.setType(type);
            transaction.setStatus(TransactionStatus.SUCCESS);

            transactionRepository.save(transaction);
            System.out.println("✅ Transaction saved successfully: " + transaction);
        } catch (Exception e) {
            System.err.println("🚨 Error saving transaction: " + e.getMessage());
        }
    }

}
