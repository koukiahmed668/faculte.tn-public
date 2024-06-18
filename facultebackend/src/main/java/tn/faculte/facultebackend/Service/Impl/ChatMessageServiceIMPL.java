package tn.faculte.facultebackend.Service.Impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Controller.WebSocketController;
import tn.faculte.facultebackend.Dto.ChatMessageDTO;
import tn.faculte.facultebackend.Dto.NotificationDTO;
import tn.faculte.facultebackend.Entity.*;
import tn.faculte.facultebackend.Repo.ChatMessageRepo;
import tn.faculte.facultebackend.Service.ChatMessageService;
import tn.faculte.facultebackend.Service.ChatRoomService;
import tn.faculte.facultebackend.Service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageServiceIMPL implements ChatMessageService {
    @Autowired
    private ChatMessageRepo chatMessageRepo;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public void sendMessage(ChatRoom chatRoom, String content, User sender, User recipient) {
        // Check if a chat room already exists between sender and recipient
        ChatRoom existingChatRoom = chatRoomService.findBySenderAndRecipient(sender, recipient);

        if (existingChatRoom == null) {
            // If no existing chat room, create a new one
            chatRoom = new ChatRoom(); // Create a new chat room instance
            chatRoom.setSender(sender);
            chatRoom.setRecipient(recipient);
            chatRoom = chatRoomService.save(chatRoom); // Save the new chat room
        } else {
            // Use the existing chat room
            chatRoom = existingChatRoom;
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(content);
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient);
        chatMessage.setStatus(MessageStatus.SENT);
        chatMessage.setCreatedAt(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);

        chatMessageRepo.save(chatMessage);

        // Notify recipient about the new message
        messagingTemplate.convertAndSendToUser(recipient.getFirstName(), "/topic/messages", chatMessage);

        // Notify sender (optional)
        messagingTemplate.convertAndSendToUser(sender.getFirstName(), "/topic/messages", chatMessage);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have received a new message from " + sender.getFirstName());
        notificationDTO.setType(NotificationType.MESSAGE);
        notificationDTO.setSenderId(sender.getId());
        notificationDTO.setReceiverId(recipient.getId());

        notificationService.sendNotification(notificationDTO, sender.getId(), recipient.getId());

        webSocketController.handleNotification(notificationDTO, null);

    }


    @Override
    public List<ChatMessage> getMessagesForChatRoom(Long chatRoomId) {
        return chatMessageRepo.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }

    @Override
    @Transactional
    public ChatMessageDTO editMessage(Long messageId, String newContent) {
        Optional<ChatMessage> messageOptional = chatMessageRepo.findById(messageId);

        // Check if the message exists
        if (messageOptional.isEmpty()) {
            // Return null or throw an exception as per your requirement
            return null;
        }

        ChatMessage chatMessage = messageOptional.get();

        // Update the message content
        chatMessage.setContent(newContent);
        chatMessageRepo.save(chatMessage);

        // Send the original ChatMessage object through WebSocket
        messagingTemplate.convertAndSend("/topic/editMessage", chatMessage);

        return modelMapper.map(chatMessage,ChatMessageDTO.class);

    }


    @Override
    public Optional<ChatMessage> getMessageById(Long messageId) {
        return chatMessageRepo.findById(messageId);
    }


}
