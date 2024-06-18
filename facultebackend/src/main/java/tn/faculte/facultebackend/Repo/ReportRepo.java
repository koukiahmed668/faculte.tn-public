package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Report;
import tn.faculte.facultebackend.Entity.ReportStatus;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface ReportRepo extends JpaRepository<Report,Long> {
    List<Report> findByStatus(ReportStatus reportStatus);

    List<Report> findAllByReportedPostIsNotNull();

    List<Report> findAllByReportedCommentIsNotNull();
}
