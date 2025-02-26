package com.example.NotificationService;

import com.example.NotificationService.NotificationStatus;
import com.example.NotificationService.Notification;
import com.example.NotificationService.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification sendNotification(Notification notification) {
        // Simulate sending a notification (e.g., SMS or email)
        // Here we simply mark it as SENT and save it.
        notification.setStatus(NotificationStatus.SENT);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}

