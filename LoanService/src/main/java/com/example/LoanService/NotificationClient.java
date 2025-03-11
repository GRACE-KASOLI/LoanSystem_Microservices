package com.example.LoanService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationClient {

    private final RestTemplate restTemplate;

    // Inject the Notification Service URL from application.properties
    @Value("${notification.service.url}")
    private String notificationServiceUrl; // e.g., http://localhost:9096/notifications/send

    public NotificationClient() {
        this.restTemplate = new RestTemplate();
    }

    public String sendNotification(Long userId, Long loanId, String message) {
        // Build the request payload as a Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", userId);
        requestBody.put("loanId", loanId);
        requestBody.put("message", message);

        ResponseEntity<String> response = restTemplate.postForEntity(notificationServiceUrl, requestBody, String.class);
        return response.getBody();
    }

}

