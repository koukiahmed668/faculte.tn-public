package tn.faculte.facultebackend.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.NotificationDTO;
import tn.faculte.facultebackend.Entity.Notification;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Repo.NotificationRepo;
import tn.faculte.facultebackend.Repo.UserRepo;
import tn.faculte.facultebackend.Service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepository;
    @Autowired
    private UserRepo userRepository;


    @Override
    public void sendNotification(NotificationDTO notificationDTO, Long senderId, Long recipientId) {
        // Fetch sender and recipient entities from database
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        // Create Notification entity
        Notification notification = new Notification();
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());
        notification.setSender(sender);
        notification.setReceiver(recipient);

        // Set other properties as needed

        // Save notification entity
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDTO> getAllNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverId(userId);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setType(notification.getType());

        notificationDTO.setSenderId(notification.getSender().getId());
        notificationDTO.setReceiverId(notification.getReceiver().getId());

        return notificationDTO;
    }


    @Override
    public void updateNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationRepository.findById(notificationDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        // Update notification message
        notification.setMessage(notificationDTO.getMessage());


        // Save updated notification entity
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }


}
