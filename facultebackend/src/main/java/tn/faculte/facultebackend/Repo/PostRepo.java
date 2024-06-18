package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Entity.EnrollmentYear;
import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Entity.Subject;

import java.util.List;
@EnableJpaRepositories
@Repository
public interface PostRepo extends JpaRepository<Post,Long> {

    List<Post> findBySubjectId(Long subjectId);
    List<Post> findByAuthorId(Long authorId);


    List<Post> findBySubject(Subject subject);

    List<Post> findBySubjectIn(List<Subject> subjects);

    List<Post> findByContentContaining(String query);

    List<Post> findByAuthorFirstNameContaining(String query);

    List<Post> findByAuthorLastNameContaining(String query);
}
