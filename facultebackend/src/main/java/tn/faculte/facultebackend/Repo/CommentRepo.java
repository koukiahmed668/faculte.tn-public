package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Comment;
import tn.faculte.facultebackend.Entity.Like;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long commentId);

    List<Comment> findByPostId(Long postId);

}
