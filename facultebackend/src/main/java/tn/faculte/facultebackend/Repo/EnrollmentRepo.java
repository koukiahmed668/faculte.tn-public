package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Enrollment;
import tn.faculte.facultebackend.Entity.User;

@EnableJpaRepositories
@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollment,Long> {
    Enrollment findByStudent(User user);
}
