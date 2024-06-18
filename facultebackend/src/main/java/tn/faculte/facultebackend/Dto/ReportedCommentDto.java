package tn.faculte.facultebackend.Dto;

import tn.faculte.facultebackend.Entity.ReportStatus;

public class ReportedCommentDto {
    private CommentDto commentDto;
    private ReportStatus status;
    private String reason;

    public ReportedCommentDto(CommentDto commentDto, ReportStatus status, String reason) {
        this.commentDto = commentDto;
        this.status = status;
        this.reason = reason;
    }

    public CommentDto getCommentDto() {
        return commentDto;
    }

    public void setCommentDto(CommentDto commentDto) {
        this.commentDto = commentDto;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
