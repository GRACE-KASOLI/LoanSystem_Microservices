package com.example.LoanService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RepaymentService {
    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RestTemplate restTemplate; // For calling M-Pesa Service

    public Repayment recordRepaymentTransaction(TransactionRequest repaymentRequest) {
        Repayment repayment = new Repayment();
        Optional<Loan> loanOpt = loanRepository.findById(repaymentRequest.getLoanId());
        if (loanOpt.isPresent()) {
            repayment.setLoan(loanOpt.get());
        } else {
            throw new RuntimeException("Loan not found for ID: " + repaymentRequest.getLoanId());
        }
        // Changed: Removed conversion since repaymentRequest.getAmount() is now a BigDecimal
        repayment.setAmount(repaymentRequest.getAmount());
        repayment.setPaymentStatus(RepaymentStatus.CONFIRMED);
        return repaymentRepository.save(repayment);
    }

    public Repayment makeRepayment(Long loanId, BigDecimal amount) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();

            // Create repayment record
            Repayment repayment = new Repayment();
            repayment.setLoan(loan);
            repayment.setAmount(amount);
            repayment.setPaymentStatus(RepaymentStatus.CONFIRMED);

            // Save repayment
            Repayment savedRepayment = repaymentRepository.save(repayment);

            // Send repayment details to M-Pesa Service
            TransactionRequest paymentRequest = new TransactionRequest();
            paymentRequest.setLoanId(loanId);
            paymentRequest.setPhoneNumber(loan.getPhoneNumber());
            paymentRequest.setAmount(amount);
            paymentRequest.setType("REPAYMENT");

            // Convert object to JSON string for debugging
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonPayload = objectMapper.writeValueAsString(paymentRequest);
                System.out.println("Sending request to M-Pesa: " + jsonPayload);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Accept", "application/json"); // Ensure JSON response

            // Wrap request with headers
            HttpEntity<TransactionRequest> requestEntity = new HttpEntity<>(paymentRequest, headers);

            // Call M-Pesa Microservice
            String mpesaUrl = "http://localhost:9094/mpesa/pay";

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        mpesaUrl,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );
                System.out.println("Mpesa Service response: " + response.getBody());
            } catch (HttpClientErrorException e) {
                System.err.println("M-Pesa Service error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            }
            return savedRepayment;
        }
        return null;
    }

    public List<Repayment> getLoanRepayments(Long loanId) {
        return repaymentRepository.findByLoanId(loanId);
    }

    public Repayment confirmRepayment(Long repaymentId) {
        Optional<Repayment> repaymentOpt = repaymentRepository.findById(repaymentId);
        if (repaymentOpt.isPresent()) {
            Repayment repayment = repaymentOpt.get();
            repayment.setPaymentStatus(RepaymentStatus.CONFIRMED);
            return repaymentRepository.save(repayment);
        }
        return null;
    }
}
