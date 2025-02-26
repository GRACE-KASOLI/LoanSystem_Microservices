package com.example.LoanService;

import com.example.LoanService.Repayment;
import com.example.LoanService.RepaymentService;
import com.example.LoanService.LoanRepository;
import com.example.LoanService.RepaymentRepository;
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
    private RestTemplate restTemplate; // To communicate with Mpesa Service

    private static final String MPESA_SERVICE_URL = "http://localhost:9094/transactions";

    @PostMapping("/{loanId}/pay")
    public Repayment makeRepayment(@PathVariable Long loanId, @RequestParam BigDecimal amount) {
        // Process repayment in Loan Service
        Repayment repayment = repaymentService.makeRepayment(loanId, amount);

        // Prepare transaction data to send to Mpesa Service
        TransactionRequest txnRequest = new TransactionRequest();
        txnRequest.setLoanId(loanId);
        txnRequest.setPhoneNumber(repayment.getLoan().getPhoneNumber()); // Ensure Loan has phoneNumber
        txnRequest.setAmount(amount.doubleValue());
        txnRequest.setType("REPAYMENT"); // Assuming type is a String in Mpesa Service

        // Send transaction to Mpesa Service
        String mpesaResponse = restTemplate.postForObject(MPESA_SERVICE_URL + "/repay", txnRequest, String.class);
        System.out.println("Mpesa Service response: " + mpesaResponse);

        return repayment;
    }

    @GetMapping("/loan/{loanId}")
    public List<Repayment> getLoanRepayments(@PathVariable Long loanId) {
        return repaymentService.getLoanRepayments(loanId);
    }

    @PutMapping("/{repaymentId}/confirm")
    public Repayment confirmRepayment(@PathVariable Long repaymentId) {
        return repaymentService.confirmRepayment(repaymentId);
    }
}