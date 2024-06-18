package tn.faculte.facultebackend.Service;

import org.springframework.http.ResponseEntity;
import tn.faculte.facultebackend.Dto.CommentDto;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Dto.ReportedCommentDto;
import tn.faculte.facultebackend.Dto.ReportedPostDto;
import tn.faculte.facultebackend.Entity.Comment;
import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Entity.Report;

import java.util.List;

public interface ReportService {
    Report getReportById(Long id);

    Report reportPost(Post post, String reason);
    Report reportComment(Comment comment, String reason);
    List<Report> getAllReports();
    List<Report> getAllPendingReports();
    void deleteReport(Report report);
    void deleteReportedPost(Report report);
    void deleteReportedComment(Report report);
    public List<ReportedCommentDto> getReportedComments();
    public List<ReportedPostDto> getReportedPosts();
}
