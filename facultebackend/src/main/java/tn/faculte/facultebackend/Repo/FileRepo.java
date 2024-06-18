package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.FileEntity;
import tn.faculte.facultebackend.Entity.Post;

import java.io.File;
import java.util.List;
@EnableJpaRepositories
@Repository
public interface FileRepo extends JpaRepository<FileEntity, Long> {
    FileEntity findByFileUrl(String fileUrl);

    List<FileEntity> findByPost(Post post);
}
