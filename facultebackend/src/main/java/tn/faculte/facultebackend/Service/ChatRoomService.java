package tn.faculte.facultebackend.Service;

import tn.faculte.facultebackend.Dto.ChatRoomDTO;
import tn.faculte.facultebackend.Entity.ChatRoom;
import tn.faculte.facultebackend.Entity.User;

import java.util.List;

public interface ChatRoomService {
    ChatRoom save(ChatRoom chatRoom);
    ChatRoom findBySenderAndRecipient(User sender, User recipient);
    List<ChatRoom> findChatRoomsByUserId(Long userId);
    ChatRoomDTO getChatRoomById(Long roomId);


    ChatRoomDTO getChatRoomByUserIds(Long loggedInUserId, Long friendId);
}
