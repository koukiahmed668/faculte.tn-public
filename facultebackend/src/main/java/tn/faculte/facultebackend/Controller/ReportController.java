package tn.faculte.facultebackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Dto.CommentDto;
import tn.faculte.facultebackend.Dto.PostDto;
import tn.faculte.facultebackend.Dto.ReportedCommentDto;
import tn.faculte.facultebackend.Dto.ReportedPostDto;
import tn.faculte.facultebackend.Entity.Comment;
import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Entity.Report;
import tn.faculte.facultebackend.Service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/reportPost")
    public ResponseEntity<Report> reportPost(@RequestParam Long postId, @RequestParam String reason) {
        Post post = new Post();
        post.setId(postId);
        Report report = reportService.reportPost(post, reason);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    @PostMapping("/reportComment")
    public ResponseEntity<Report> reportComment(@RequestParam Long commentId, @RequestParam String reason) {
        Comment comment = new Comment();
        comment.setId(commentId);
        Report report = reportService.reportComment(comment, reason);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Report>> getAllPendingReports() {
        List<Report> pendingReports = reportService.getAllPendingReports();
        return new ResponseEntity<>(pendingReports, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        reportService.deleteReport(report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteReportedPost/{id}")
    public ResponseEntity<Void> deleteReportedPost(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        if (report == null || report.getReportedPost() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        reportService.deleteReportedPost(report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteReportedComment/{id}")
    public ResponseEntity<Void> deleteReportedComment(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        if (report == null || report.getReportedComment() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        reportService.deleteReportedComment(report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/reportedPosts")
    public ResponseEntity<List<ReportedPostDto>> getReportedPosts() {
        List<ReportedPostDto> reportedPosts = reportService.getReportedPosts();
        return new ResponseEntity<>(reportedPosts, HttpStatus.OK);
    }

    @GetMapping("/reportedComments")
    public ResponseEntity<List<ReportedCommentDto>> getReportedComments() {
        List<ReportedCommentDto> reportedComments = reportService.getReportedComments();
        return new ResponseEntity<>(reportedComments, HttpStatus.OK);
    }


}
