package com.example.MpesaService;

import org.springframework.stereotype.Service;

@Service
public class MpesaService {

    public String disburseLoan(String phoneNumber, String amount) {
        System.out.println("Disbursing Loan: " + amount + " to " + phoneNumber);
        return "Loan of " + amount + " has been disbursed to " + phoneNumber;
    }

    public String processRepayment(String phoneNumber, String amount) {
        System.out.println("Processing Repayment: " + amount + " from " + phoneNumber);
        return "Payment of " + amount + " received from " + phoneNumber;
    }
}
