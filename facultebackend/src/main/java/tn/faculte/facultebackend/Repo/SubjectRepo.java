package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.EnrollmentYear;
import tn.faculte.facultebackend.Entity.Subject;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface SubjectRepo extends JpaRepository<Subject,Long> {
    List<Subject> findBySpecialtyId(Long specialtyId);

    List<Subject> findBySpecialtyIdAndEnrollmentYear(Long specialtyId, EnrollmentYear enrollmentYear);

    List<Subject> findBySpecialtyIdAndEnrollmentYearIn(Long specialtyId, List<EnrollmentYear> primaire);
}
