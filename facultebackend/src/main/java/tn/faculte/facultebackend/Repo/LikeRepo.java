package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Like;

import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface LikeRepo extends JpaRepository<Like,Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
}
