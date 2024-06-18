package tn.faculte.facultebackend.Controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Dto.ChatMessageDTO;
import tn.faculte.facultebackend.Entity.ChatMessage;
import tn.faculte.facultebackend.Entity.ChatRoom;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.UserRepo;
import tn.faculte.facultebackend.Service.ChatMessageService;
import tn.faculte.facultebackend.Service.UserService;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);


    @MessageMapping("/messages/send")
    @SendTo("/topic/messages")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO request) {

        // Log the received message
        logger.info("Received message from sender ID {} with content: {}", request.getSenderId(), request.getContent());

        // Retrieve sender and recipient IDs from the request
        Long senderId = request.getSenderId();
        Long recipientId = request.getRecipientId();

        // Retrieve sender and recipient users from the database
        Optional<User> senderOptional = userRepo.findById(senderId);
        Optional<User> recipientOptional = userRepo.findById(recipientId);

        // Check if sender and recipient exist
        if (senderOptional.isEmpty() || recipientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender or recipient not found");
        }

        // Get sender and recipient from optionals
        User sender = senderOptional.get();
        User recipient = recipientOptional.get();

        // Create a new chat room
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setSender(sender);
        chatRoom.setRecipient(recipient);

        // Send the message using the chat message service
        chatMessageService.sendMessage(chatRoom, request.getContent(), sender, recipient);


        return ResponseEntity.ok(request);

    }


    @GetMapping("/chat-room/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessagesForChatRoom(@PathVariable Long chatRoomId) {
        // Call the service method to fetch messages for the specified chat room
        List<ChatMessage> messages = chatMessageService.getMessagesForChatRoom(chatRoomId);

        // Convert ChatMessage objects to ChatMessageDTO objects
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageDTOs);
    }

    // Helper method to convert ChatMessage to ChatMessageDTO
    private ChatMessageDTO convertToDTO(ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setStatus(message.getStatus());
        dto.setSenderId(message.getSender().getId());
        dto.setRecipientId(message.getRecipient().getId());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }

    @PutMapping("/edit/{messageId}")
    public ResponseEntity<ChatMessageDTO> editMessage(@PathVariable Long messageId, @RequestBody String newContent) {
        ChatMessageDTO editedMessage = chatMessageService.editMessage(messageId, newContent);

        // Check if the message was successfully edited
        if (editedMessage == null) {
            // Handle the case where the message does not exist
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(editedMessage);
    }



}
