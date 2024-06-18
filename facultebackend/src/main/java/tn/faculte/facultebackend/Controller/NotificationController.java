package tn.faculte.facultebackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Dto.NotificationDTO;
import tn.faculte.facultebackend.Entity.Notification;
import tn.faculte.facultebackend.Service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationDTO> notificationDTOs = notificationService.getAllNotificationsByUserId(userId);
        return ResponseEntity.ok(notificationDTOs);
    }


    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setType(notification.getType());
        return notificationDTO;
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateNotification(@RequestBody NotificationDTO notificationDTO) {
        try {
            notificationService.updateNotification(notificationDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }


}
