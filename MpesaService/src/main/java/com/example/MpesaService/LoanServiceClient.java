package com.example.MpesaService;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoanServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public Long getLoanIdByPhone(String phoneNumber) {
        String url = "http://localhost:9093/loans/getLoanIdByPhone/" + phoneNumber;
        return restTemplate.getForObject(url, Long.class);
    }
}

