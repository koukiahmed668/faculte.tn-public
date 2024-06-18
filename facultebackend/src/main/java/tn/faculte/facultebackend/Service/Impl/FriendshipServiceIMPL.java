package tn.faculte.facultebackend.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Controller.WebSocketController;
import tn.faculte.facultebackend.Dto.NotificationDTO;
import tn.faculte.facultebackend.Entity.Friendship;
import tn.faculte.facultebackend.Entity.Notification;
import tn.faculte.facultebackend.Entity.NotificationType;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.FriendshipRepo;
import tn.faculte.facultebackend.Repo.UserRepo;
import tn.faculte.facultebackend.Service.FriendshipService;
import tn.faculte.facultebackend.Service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipServiceIMPL implements FriendshipService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FriendshipRepo friendshipRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketController webSocketController;


    @Override
    public String getFriendshipStatus(Long userId1, Long userId2) {
        Optional<Friendship> friendship1 = friendshipRepo.findByRequesterIdAndReceiverId(userId1, userId2);
        Optional<Friendship> friendship2 = friendshipRepo.findByRequesterIdAndReceiverId(userId2, userId1);

        if (friendship1.isPresent() && friendship2.isPresent()) {
            if (friendship1.get().isAccepted() && friendship2.get().isAccepted()) {
                return "friends"; // Both friendships exist and are accepted
            } else if (friendship1.get().isAccepted() || friendship2.get().isAccepted()) {
                return "none"; // One friendship exists but the other one isn't accepted
            } else {
                return "received"; // Both friendships exist but neither are accepted
            }
        } else if (friendship1.isPresent()) {
            if (friendship1.get().isAccepted()) {
                return "friends"; // Friendship request sent by userId1 to userId2 and accepted
            } else {
                return "sent"; // Friendship request sent by userId1 to userId2 but not yet accepted
            }
        } else if (friendship2.isPresent()) {
            if (friendship2.get().isAccepted()) {
                return "friends"; // Friendship request sent by userId2 to userId1 and accepted
            } else {
                return "received"; // Friendship request sent by userId2 to userId1 but not yet accepted
            }
        } else {
            return "none"; // No friendship request exists between the users
        }
    }




    @Override
    public void deleteFriendship(Long userId1, Long userId2) {
        // Check if friendship exists between the two users
        Optional<Friendship> friendship1 = friendshipRepo.findByRequesterIdAndReceiverId(userId1, userId2);
        Optional<Friendship> friendship2 = friendshipRepo.findByRequesterIdAndReceiverId(userId2, userId1);

        if (friendship1.isPresent()) {
            friendshipRepo.delete(friendship1.get());
        } else if (friendship2.isPresent()) {
            friendshipRepo.delete(friendship2.get());
        } else {
            // Friendship not found
            throw new IllegalArgumentException("Friendship not found");
        }
    }











    @Override
    public void sendFriendRequest(Long requesterId, Long receiverId) {
        User requester = userRepo.findById(requesterId).orElseThrow(() -> new IllegalArgumentException("Invalid requester ID"));
        User receiver = userRepo.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("Invalid receiver ID"));

        // Check if friendship already exists or if requester and receiver are the same
        if (friendshipRepo.existsByRequesterAndReceiver(requester, receiver) ||
                friendshipRepo.existsByRequesterAndReceiver(receiver, requester) ||
                requesterId.equals(receiverId)) {
            throw new IllegalArgumentException("Invalid friend request");
        }

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setReceiver(receiver);
        friendship.setAccepted(false); // Initially, the request is not accepted
        friendshipRepo.save(friendship);

        // Create and send notification to the receiver

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have received a friend request from " + requester.getFirstName());
        notificationDTO.setType(NotificationType.FRIEND_REQUEST);
        notificationDTO.setSenderId(requesterId);
        notificationDTO.setReceiverId(receiverId);

        notificationService.sendNotification(notificationDTO,requesterId,receiverId);

        webSocketController.handleNotification(notificationDTO, null);

    }

    @Override
    public void cancelFriendRequest(Long requesterId, Long receiverId) {
        Optional<Friendship> friendship = friendshipRepo.findByRequesterIdAndReceiverId(requesterId, receiverId);
        if (friendship.isPresent()) {
            friendshipRepo.delete(friendship.get());
        } else {
            throw new IllegalArgumentException("No friend request found");
        }
    }

    @Override
    public void acceptFriendRequest(Long loggedInUserId, Long userId) {
        Optional<Friendship> friendship = friendshipRepo.findByRequesterIdAndReceiverId(userId, loggedInUserId);
        if (friendship.isPresent()) {
            friendship.get().setAccepted(true);
            friendshipRepo.save(friendship.get());
        } else {
            throw new IllegalArgumentException("No friend request found");
        }
    }

    @Override
    public void rejectFriendRequest(Long loggedInUserId, Long userId) {
        Optional<Friendship> friendship = friendshipRepo.findByRequesterIdAndReceiverId(userId, loggedInUserId);
        if (friendship.isPresent()) {
            friendshipRepo.delete(friendship.get());
        } else {
            throw new IllegalArgumentException("No friend request found");
        }
    }


    @Override
    public List<User> getFriends(Long userId) {
        List<Friendship> acceptedFriendships = friendshipRepo.findAcceptedFriendshipsByUserId(userId);
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : acceptedFriendships) {
            // Determine which user in the friendship is not the current user
            User friend = friendship.getRequester().getId().equals(userId) ? friendship.getReceiver() : friendship.getRequester();
            friends.add(friend);
        }
        return friends;
    }




}
