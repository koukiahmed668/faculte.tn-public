package tn.faculte.facultebackend.Service;

import tn.faculte.facultebackend.Entity.User;

import java.util.List;

public interface FriendshipService {
    String getFriendshipStatus(Long loggedInUserId, Long userId);
    void sendFriendRequest(Long requesterId, Long receiverId);
    public void deleteFriendship(Long userId1, Long userId2);
    void cancelFriendRequest(Long requesterId, Long receiverId);
    void acceptFriendRequest(Long loggedInUserId, Long userId);
    void rejectFriendRequest(Long loggedInUserId, Long userId);
    List<User> getFriends(Long userId);

}
