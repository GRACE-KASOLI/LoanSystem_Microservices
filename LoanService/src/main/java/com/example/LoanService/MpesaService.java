package com.example.LoanService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MpesaService {

    private final RestTemplate restTemplate;
    private final String mpesaBaseUrl = "http://localhost:9094/mpesa"; // M-Pesa Microservice URL

    public MpesaService() {
        this.restTemplate = new RestTemplate();
    }

    public String requestLoanDisbursement(String phoneNumber, String amount) {
        String url = mpesaBaseUrl + "/disburse?phoneNumber=" + phoneNumber + "&amount=" + amount;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        return response.getBody();
    }

    public String requestLoanRepayment(String phoneNumber, String amount) {
        String url = mpesaBaseUrl + "/pay?phoneNumber=" + phoneNumber + "&amount=" + amount;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        return response.getBody();
    }
}

