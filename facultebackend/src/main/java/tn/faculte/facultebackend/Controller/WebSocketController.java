package tn.faculte.facultebackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.faculte.facultebackend.Dto.NotificationDTO;
import tn.faculte.facultebackend.Entity.ChatMessage;
import tn.faculte.facultebackend.Entity.Notification;

import java.security.Principal;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/notifications/send")
    public void handleNotification(NotificationDTO notificationDTO, Principal principal) {
        Long recipientUserId = notificationDTO.getReceiverId();
        messagingTemplate.convertAndSendToUser(
                String.valueOf(recipientUserId),
                "/queue/notifications",
                notificationDTO
        );
        System.out.println("Notification sent to user with ID: " + recipientUserId);
    }

    @MessageMapping("/editMessage")
    public void editMessage(@Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/topic/editMessage", chatMessage);
    }


}

