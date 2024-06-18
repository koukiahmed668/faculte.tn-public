package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Major;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface MajorRepo extends JpaRepository<Major,Long> {
    Major findByName(String name);

}
