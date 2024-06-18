package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Notification;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface NotificationRepo extends JpaRepository<Notification,Long> {
    List<Notification> findByReceiverId(Long userId);
}
