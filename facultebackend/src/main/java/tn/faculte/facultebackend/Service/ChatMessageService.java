package tn.faculte.facultebackend.Service;

import tn.faculte.facultebackend.Dto.ChatMessageDTO;
import tn.faculte.facultebackend.Entity.ChatMessage;
import tn.faculte.facultebackend.Entity.ChatRoom;
import tn.faculte.facultebackend.Entity.User;

import java.util.List;
import java.util.Optional;

public interface ChatMessageService {
    void sendMessage(ChatRoom chatRoom, String content, User sender, User recipient);
    List<ChatMessage> getMessagesForChatRoom(Long chatRoomId);
    public ChatMessageDTO editMessage(Long messageId, String newContent);
    Optional<ChatMessage> getMessageById(Long messageId);

}
