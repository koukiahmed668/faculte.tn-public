package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.CourseStatus;
import tn.faculte.facultebackend.Entity.PaidCourse;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface PaidCourseRepo extends JpaRepository<PaidCourse,Long> {
    List<PaidCourse> findByStatus(CourseStatus courseStatus);
}
