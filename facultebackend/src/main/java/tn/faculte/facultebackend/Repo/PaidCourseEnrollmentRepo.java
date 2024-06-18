package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.PaidCourseEnrollment;
import tn.faculte.facultebackend.Entity.User;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface PaidCourseEnrollmentRepo extends JpaRepository<PaidCourseEnrollment,Long> {
    List<PaidCourseEnrollment> findByUser(User user);

    List<PaidCourseEnrollment> findByUserId(Long userId);

    List<PaidCourseEnrollment> findByPaidCourseId(Long courseId);
}
