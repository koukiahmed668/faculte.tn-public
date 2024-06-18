package tn.faculte.facultebackend.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.faculte.facultebackend.Dto.ChatRoomDTO;
import tn.faculte.facultebackend.Entity.ChatRoom;
import tn.faculte.facultebackend.Service.ChatRoomService;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoom>> getChatRoomsByUserId(@PathVariable Long userId) {
        List<ChatRoom> chatRooms = chatRoomService.findChatRoomsByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomDTO> getChatRoomById(@PathVariable Long roomId) {
        ChatRoomDTO chatRoom = chatRoomService.getChatRoomById(roomId);
        if (chatRoom != null) {
            return ResponseEntity.ok(chatRoom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{loggedInUserId}/friend/{friendId}")
    public ResponseEntity<ChatRoomDTO> getChatRoomByUserIds(@PathVariable Long loggedInUserId, @PathVariable Long friendId) {
        ChatRoomDTO chatRoom = chatRoomService.getChatRoomByUserIds(loggedInUserId, friendId);
        if (chatRoom != null) {
            return ResponseEntity.ok(chatRoom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}