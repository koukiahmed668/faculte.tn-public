package tn.faculte.facultebackend.Dto;

import tn.faculte.facultebackend.Entity.ReportStatus;

public class ReportedPostDto {
    private PostDto postDto;
    private ReportStatus status;
    private String reason;

    public ReportedPostDto(PostDto postDto, ReportStatus status, String reason) {
        this.postDto = postDto;
        this.status = status;
        this.reason = reason;
    }

    public PostDto getPostDto() {
        return postDto;
    }

    public void setPostDto(PostDto postDto) {
        this.postDto = postDto;
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
