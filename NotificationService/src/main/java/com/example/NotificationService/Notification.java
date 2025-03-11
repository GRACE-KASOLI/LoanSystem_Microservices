package com.example.NotificationService;

import com.example.NotificationService.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;  // ID of the user to be notified
    private Long loanId;  // Optional, if related to a loan event
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.SENT;

    private LocalDateTime createdAt = LocalDateTime.now();
}

