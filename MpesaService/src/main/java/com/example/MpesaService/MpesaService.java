package com.example.MpesaService;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class MpesaService {

    // Ensured JSON format support
    public String disburseLoan(String phoneNumber, String amount) {
        System.out.println("Disbursing Loan: " + amount + " to " + phoneNumber);
        return "{\"message\": \"Loan of " + amount + " has been disbursed to " + phoneNumber + "\"}";
    }

    // Added loanId and paymentStatus while ensuring JSON response format
    public String processRepayment(Long loanId, String phoneNumber, String amount, String paymentStatus) {
        System.out.println("Processing Repayment for LoanId: " + loanId + " - " + amount + " from " + phoneNumber + " with status " + paymentStatus);
        return "{\"message\": \"Payment of " + amount + " received from " + phoneNumber + "\"}";
    }
}
