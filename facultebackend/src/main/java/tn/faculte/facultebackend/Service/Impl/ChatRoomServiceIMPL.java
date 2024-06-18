package tn.faculte.facultebackend.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.ChatRoomDTO;
import tn.faculte.facultebackend.Entity.ChatRoom;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.ChatRoomRepo;
import tn.faculte.facultebackend.Repo.UserRepo;
import tn.faculte.facultebackend.Service.ChatRoomService;

import java.util.List;

@Service
public class ChatRoomServiceIMPL implements ChatRoomService {

    @Autowired
    private ChatRoomRepo chatRoomRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepo.save(chatRoom);
    }
    @Override
    public ChatRoom findBySenderAndRecipient(User user1, User user2) {
        // Find a chat room where either user1 or user2 is the sender and either user1 or user2 is the recipient
        ChatRoom chatRoom1 = chatRoomRepo.findBySenderAndRecipient(user1, user2);
        ChatRoom chatRoom2 = chatRoomRepo.findBySenderAndRecipient(user2, user1);

        // Return chatRoom1 if found, otherwise return chatRoom2
        return chatRoom1 != null ? chatRoom1 : chatRoom2;
    }

    @Override
    public List<ChatRoom> findChatRoomsByUserId(Long userId) {
        // Implement the logic to find chat rooms by user ID
        return chatRoomRepo.findChatRoomsByUserId(userId);
    }

    @Override
    public ChatRoomDTO getChatRoomById(Long roomId) {
        ChatRoom chatRoom = chatRoomRepo.findById(roomId).orElse(null);
        if (chatRoom != null) {
            return new ChatRoomDTO(chatRoom.getId(), chatRoom.getSender().getId(), chatRoom.getRecipient().getId());
        }
        return null;
    }

    @Override
    public ChatRoomDTO getChatRoomByUserIds(Long loggedInUserId, Long friendId) {
        // Find the chat room between loggedInUserId and friendId, if it exists
        User loggedInUser = userRepo.findById(loggedInUserId).orElse(null);
        User friend = userRepo.findById(friendId).orElse(null);
        if (loggedInUser != null && friend != null) {
            // Check for the existence of the chat room in both possible sender-recipient combinations
            ChatRoom chatRoom1 = chatRoomRepo.findBySenderAndRecipient(loggedInUser, friend);
            ChatRoom chatRoom2 = chatRoomRepo.findBySenderAndRecipient(friend, loggedInUser);

            // Return the chat room if found in either direction
            if (chatRoom1 != null) {
                return new ChatRoomDTO(chatRoom1.getId(), loggedInUserId, friendId);
            } else if (chatRoom2 != null) {
                return new ChatRoomDTO(chatRoom2.getId(), friendId, loggedInUserId);
            } else {
                // If no chat room is found, create a new chat room
                ChatRoom newChatRoom = new ChatRoom(loggedInUser, friend);
                chatRoomRepo.save(newChatRoom);
                return new ChatRoomDTO(newChatRoom.getId(), loggedInUserId, friendId);
            }
        }
        return null; // Return null if either user is not found
    }



}
