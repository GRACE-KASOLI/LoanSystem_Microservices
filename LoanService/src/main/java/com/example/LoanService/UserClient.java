package com.example.LoanService;

import com.example.LoanService.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url}") // e.g., http://localhost:9097
    private String userServiceUrl;

    public User getUserById(Long userId) {
        String url = userServiceUrl + "/users/" + userId;
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
        return response.getBody();
    }
}
