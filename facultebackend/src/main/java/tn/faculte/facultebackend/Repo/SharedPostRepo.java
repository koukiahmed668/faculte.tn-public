package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.SharedPost;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface SharedPostRepo extends JpaRepository<SharedPost,Long> {
    List<SharedPost> findBySharingUserId(Long userId);
}
