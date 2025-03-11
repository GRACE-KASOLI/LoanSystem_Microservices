package com.example.MpesaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LoanServiceClient loanServiceClient;

    @Autowired
    private RestTemplate restTemplate;  // For calling Loan Management Microservice API

    private final String Loan_Service_URL = "http://localhost:9093/loans"; // Update with actual URL

    public Transactions saveTransaction(Transactions transaction) {
        // Fetch loanId from Loan Microservice
        logger.info("Received transaction request for phone: {}", transaction.getPhoneNumber());
        Optional<Long> loanIdOpt = fetchLoanIdByPhoneNumber(transaction.getPhoneNumber());

        if (loanIdOpt.isEmpty()) {
            logger.error("No active loan found for phone number: {}", transaction.getPhoneNumber());
            throw new RuntimeException("No active loan found for phone number: " + transaction.getPhoneNumber());
        }

        transaction.setLoanId(loanIdOpt.get());
        logger.info("Saving transaction with loanId: {}", transaction.getLoanId());
        return transactionRepository.save(transaction);
    }

    public Optional<Long> fetchLoanIdByPhoneNumber(String phoneNumber) {
        String url = Loan_Service_URL + "/getLoanId?phoneNumber=" + phoneNumber;
        System.out.println("Calling Loan Microservice: " + url); // Debugging

        Long loanId = restTemplate.getForObject(url, Long.class);

        System.out.println("Loan Service Response: " + loanId); // Debugging

        return Optional.ofNullable(loanId);
    }
}
