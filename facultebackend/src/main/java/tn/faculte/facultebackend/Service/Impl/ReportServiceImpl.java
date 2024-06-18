package tn.faculte.facultebackend.Service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Dto.CommentDto;
import tn.faculte.facultebackend.Dto.ReportedCommentDto;
import tn.faculte.facultebackend.Dto.ReportedPostDto;
import tn.faculte.facultebackend.Entity.Comment;
import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Entity.Report;
import tn.faculte.facultebackend.Entity.ReportStatus;
import tn.faculte.facultebackend.Repo.CommentRepo;
import tn.faculte.facultebackend.Repo.PostRepo;
import tn.faculte.facultebackend.Repo.ReportRepo;
import tn.faculte.facultebackend.Service.ReportService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepo reportRepository;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }


    @Override
    public Report reportPost(Post post, String reason) {
        Report report = new Report();
        report.setReportedPost(post);
        report.setReason(reason);
        report.setStatus(ReportStatus.PENDING);
        return reportRepository.save(report);
    }

    @Override
    public Report reportComment(Comment comment, String reason) {
        Report report = new Report();
        report.setReportedComment(comment);
        report.setReason(reason);
        report.setStatus(ReportStatus.PENDING);
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public List<Report> getAllPendingReports() {
        return reportRepository.findByStatus(ReportStatus.PENDING);
    }

    @Override
    public void deleteReport(Report report) {
        reportRepository.delete(report);
    }

    @Override
    public void deleteReportedPost(Report report) {
        if (report.getReportedPost() != null) {
            // Delete the reported post
            postRepo.delete(report.getReportedPost());
            // Delete the report
            reportRepository.delete(report);
        }
    }


    @Override
    public void deleteReportedComment(Report report) {
        if (report.getReportedComment() != null) {
            // Delete the reported comment
            // Assuming that comment deletion is straightforward
            commentRepo.delete(report.getReportedComment());

            // Delete the report
            reportRepository.delete(report);
        }
    }

    @Override
    public List<ReportedPostDto> getReportedPosts() {
        List<Report> reportedPosts = reportRepository.findAllByReportedPostIsNotNull();
        return reportedPosts.stream()
                .map(report -> new ReportedPostDto(
                        modelMapper.map(report.getReportedPost(), PostDto.class),
                        report.getStatus(),
                        report.getReason()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportedCommentDto> getReportedComments() {
        List<Report> reportedComments = reportRepository.findAllByReportedCommentIsNotNull();
        return reportedComments.stream()
                .map(report -> new ReportedCommentDto(
                        modelMapper.map(report.getReportedComment(), CommentDto.class),
                        report.getStatus(),
                        report.getReason()))
                .collect(Collectors.toList());
    }

}
