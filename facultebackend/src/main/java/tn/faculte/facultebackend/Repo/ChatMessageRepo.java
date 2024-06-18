package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.ChatMessage;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ChatMessageRepo extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
}
