package tn.faculte.facultebackend.Service;

import tn.faculte.facultebackend.Dto.NotificationDTO;
import tn.faculte.facultebackend.Entity.Notification;

import java.util.List;

public interface NotificationService {
    public void sendNotification(NotificationDTO notificationDTO, Long senderId, Long recipientId);
    List<NotificationDTO> getAllNotificationsByUserId(Long userId);
    public void updateNotification(NotificationDTO notificationDTO);
    void deleteNotification(Long notificationId);

}
