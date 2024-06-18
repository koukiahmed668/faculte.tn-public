package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Bookmark;
import tn.faculte.facultebackend.Entity.Post;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface BookmarkRepo extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserId(Long userId);
    Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);
    @Query("SELECT b.post FROM Bookmark b WHERE b.user.id = :userId")
    List<Post> findBookmarkedPostsByUserId(@Param("userId") Long userId);
}
