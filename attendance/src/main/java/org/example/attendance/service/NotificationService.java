package org.example.attendance.service;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.example.attendance.entity.Notification;
import org.example.attendance.repository.NotificationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepo notificationRepo;

    public void createNotification(Long receiverId, String title, String message, String type, Long relatedId) {
        Notification noti = new Notification();
        noti.setReceiverId(receiverId);
        noti.setTitle(title);
        noti.setMessage(message);
        noti.setType(type);
        noti.setRelatedId(relatedId);
        noti.setRead(false);
        notificationRepo.save(noti);
    }

    public List<Notification> getMyNotifications(Long userId) {
        return notificationRepo.findByReceiverIdOrderByCreatedAtDesc(userId);
    }

    public long countUnread(Long userId) {
        return notificationRepo.countByReceiverIdAndIsReadFalse(userId);
    }

    public void markAsRead(Long notiId) {
        Notification noti = notificationRepo.findById(notiId).orElse(null);
        if (noti != null) {
            noti.setRead(true);
            notificationRepo.save(noti);
        }
    }
    public void sendNotification(Long userId, String message) {
        Notification notification = new Notification();

        notification.setReceiverId(userId);

        notification.setMessage(message);
        notification.setTitle("Notification system");
        notification.setRead(false);

        notificationRepo.save(notification);
    }
}