package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Specialty;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SpecialtyRepo extends JpaRepository<Specialty,Long> {
    List<Specialty> findByMajorId(Long majorId);
    Specialty findByName(String name);

}
