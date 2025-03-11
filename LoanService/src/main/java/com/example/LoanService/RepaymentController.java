package com.example.LoanService;

import com.example.LoanService.Repayment;
import com.example.LoanService.RepaymentService;
import com.example.LoanService.LoanRepository;
import com.example.LoanService.RepaymentRepository;
import com.example.LoanService.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/repayments")
public class RepaymentController {
    @Autowired
    private RepaymentService repaymentService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RestTemplate restTemplate; // To communicate with Mpesa Service

    private static final String MPESA_SERVICE_URL = "http://localhost:9094/mpesa";
    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:9096/notifications";

    @PostMapping("/{loanId}/pay")
    public Repayment makeRepayment(@PathVariable Long loanId, @RequestParam BigDecimal amount) {
        // Retrieve userId related to loanId
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
        Long userId = loan.getUserId();

        // Process repayment in Loan Service
        Repayment repayment = repaymentService.makeRepayment(loanId, amount);

        // Prepare transaction data to send to Mpesa Service
        TransactionRequest txnRequest = new TransactionRequest();
        txnRequest.setLoanId(loanId);
        txnRequest.setUserId(userId);
        txnRequest.setPhoneNumber(loan.getPhoneNumber()); // Ensure Loan has phoneNumber
        txnRequest.setAmount(amount);
        txnRequest.setType("REPAYMENT"); // Assuming type is a String in Mpesa Service

        // Send transaction to Mpesa Service
        String mpesaResponse = restTemplate.postForObject(MPESA_SERVICE_URL + "/pay", txnRequest, String.class);
        System.out.println("Mpesa Service response: " + mpesaResponse);

        // Send notification after payment is saved
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(userId);
        notificationRequest.setLoanId(loanId);
        notificationRequest.setMessage("Your payment for loan ID: " + loanId + " has been received successfully.");
        notificationRequest.setPhoneNumber(loan.getPhoneNumber());

        restTemplate.postForObject(NOTIFICATION_SERVICE_URL + "/send", notificationRequest, String.class);

        return repayment;
    }

    @GetMapping("/loan/{loanId}")
    public List<Repayment> getLoanRepayments(@PathVariable Long loanId) {
        return repaymentService.getLoanRepayments(loanId);
    }

    @PutMapping("/{repaymentId}")
    public Repayment confirmRepayment(@PathVariable Long repaymentId) {
        return repaymentService.confirmRepayment(repaymentId);
    }
}
