package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;
import tn.faculte.facultebackend.Entity.Friendship;
import tn.faculte.facultebackend.Entity.User;

import java.util.List;
import java.util.Optional;

@RestController
@EnableJpaRepositories
public interface FriendshipRepo extends JpaRepository<Friendship,Long> {


    boolean existsByRequesterAndReceiver(User requester, User receiver);
    @Query("SELECT CASE WHEN f.requester = :user THEN f.receiver ELSE f.requester END FROM Friendship f WHERE f.requester = :user OR f.receiver = :user")

    List<User> findFriendsByUser(User user);
    @Query("SELECT f FROM Friendship f WHERE (f.requester.id = :userId OR f.receiver.id = :userId) AND f.accepted = true")
    List<Friendship> findAcceptedFriendshipsByUserId(Long userId);

    Optional<Friendship> findByRequesterIdAndReceiverId(Long loggedInUserId, Long userId);

    @Query("SELECT f FROM Friendship f WHERE ((f.requester.id = :requesterId AND f.receiver.id = :receiverId) OR (f.requester.id = :receiverId AND f.receiver.id = :requesterId)) AND f.accepted = true")
    Optional<Friendship> findAcceptedFriendship(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId);


    boolean existsByRequesterIdAndReceiverId(Long requesterId, Long receiverId);
    Optional<Friendship> findAcceptedFriendshipByRequesterIdAndReceiverIdAndAcceptedIsTrue(Long requesterId, Long receiverId);

}
