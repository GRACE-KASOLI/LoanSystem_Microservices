package com.example.LoanService;

import com.example.LoanService.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loans")
@CrossOrigin(origins = "http://localhost:4200") // Allow frontend requests
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    @PostMapping("/apply")
    public ResponseEntity<Loan> applyForLoan(@RequestBody Loan loan) {
        return ResponseEntity.ok(loanService.applyForLoan(loan));
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Optional<Loan>> getLoanById(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }

    // Ensures database transactions are committed
    @PutMapping("/{loanId}/status")
    public ResponseEntity<Loan> updateLoanStatus(@PathVariable Long loanId, @RequestParam String status) {
        Loan updatedLoan = loanService.updateLoanStatus(loanId, status);

        if ("DISBURSED".equalsIgnoreCase(updatedLoan.getStatus().name())) {
            // Save transaction in M-Pesa microservice
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setLoanId(updatedLoan.getId());
            transactionRequest.setPhoneNumber(updatedLoan.getPhoneNumber());
            transactionRequest.setAmount(updatedLoan.getAmount());
            transactionRequest.setStatus("SUCCESS");
            transactionRequest.setType("DISBURSEMENT");

            String mpesaUrl = "http://localhost:9094/mpesa/disburse";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TransactionRequest> transactionEntity = new HttpEntity<>(transactionRequest, headers);

            try {
                ResponseEntity<String> transactionResponse = restTemplate.postForEntity(mpesaUrl, transactionEntity, String.class);
                System.out.println("M-Pesa Transaction Response: " + transactionResponse.getBody());
            } catch (Exception e) {
                System.err.println("Error saving transaction in M-Pesa service: " + e.getMessage());
            }

            // Send notification
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setMessage("Your loan of " + updatedLoan.getAmount() + " has been disbursed.");
            notificationRequest.setPhoneNumber(updatedLoan.getPhoneNumber());
            notificationRequest.setLoanId(updatedLoan.getId());
            notificationRequest.setUserId(updatedLoan.getUserId());

            String notificationServiceUrl = "http://localhost:9096/notifications/send";
            HttpEntity<NotificationRequest> notificationEntity = new HttpEntity<>(notificationRequest, headers);

            try {
                ResponseEntity<String> notificationResponse = restTemplate.postForEntity(notificationServiceUrl, notificationEntity, String.class);
                System.out.println("Notification Response: " + notificationResponse.getBody());
            } catch (Exception e) {
                System.err.println("Error sending notification: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(updatedLoan);
    }

    @GetMapping("/getLoanId")
    public ResponseEntity<Long> getLoanIdByPhoneNumber(@RequestParam String phoneNumber) {
        Optional<Long> loanId = loanService.getActiveLoanIdByphoneNumber(phoneNumber);
        return loanId.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/count")
    public long getCountByStatus(@RequestParam String status) {
        return loanService.countLoansByStatus(status);
    }
    @PutMapping("/{loanId}")
    public Loan updateLoan(@PathVariable Long loanId, @RequestBody Loan updatedLoan) {
        return loanService.updateLoan(loanId, updatedLoan);
    }
    @DeleteMapping("/{loanId}")
    public void deleteLoan(@PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
    }

}
