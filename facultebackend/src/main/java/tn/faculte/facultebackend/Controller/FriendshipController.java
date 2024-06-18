package tn.faculte.facultebackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Service.FriendshipService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    // Remove the loggedInUserId field

    @PostMapping("/friend-request/{requesterId}/{receiverId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long requesterId, @PathVariable Long receiverId) {
        friendshipService.sendFriendRequest(requesterId, receiverId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel-friend-request/{requesterId}/{receiverId}")
    public ResponseEntity<?> cancelFriendRequest(@PathVariable Long requesterId, @PathVariable Long receiverId) {
        friendshipService.cancelFriendRequest(requesterId, receiverId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/accept-friend-request/{userId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long userId, @RequestParam Long loggedInUserId) {
        friendshipService.acceptFriendRequest(loggedInUserId, userId); // Pass logged-in user ID as a request parameter
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject-friend-request/{userId}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Long userId, @RequestParam Long loggedInUserId) {
        friendshipService.rejectFriendRequest(loggedInUserId, userId); // Pass logged-in user ID as a request parameter
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long userId) {
        List<User> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{requesterId}/{receiverId}")
    public ResponseEntity<Map<String, String>> getFriendshipStatus(@PathVariable Long requesterId, @PathVariable Long receiverId) {
        String status = friendshipService.getFriendshipStatus(requesterId, receiverId);
        Map<String, String> response = Collections.singletonMap("status", status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-friendship/{userId1}/{userId2}")
    public ResponseEntity<?> deleteFriendship(@PathVariable Long userId1, @PathVariable Long userId2) {
        friendshipService.deleteFriendship(userId1, userId2);
        return ResponseEntity.ok().build();
    }


}
