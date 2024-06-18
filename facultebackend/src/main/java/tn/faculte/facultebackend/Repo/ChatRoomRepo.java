package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.ChatRoom;
import tn.faculte.facultebackend.Entity.User;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ChatRoomRepo extends JpaRepository<ChatRoom,Long> {
    ChatRoom findBySenderAndRecipient(User sender, User recipient);
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.sender.id = :userId OR cr.recipient.id = :userId")
    List<ChatRoom> findChatRoomsByUserId(Long userId);

}
