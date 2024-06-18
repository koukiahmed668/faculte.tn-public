package tn.faculte.facultebackend.Dto;

import tn.faculte.facultebackend.Entity.Comment;
import tn.faculte.facultebackend.Entity.Like;
import tn.faculte.facultebackend.Entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDto {

    private Long id;
    private String content;
    private User author;
    private String authorFirstName;
    private String authorLastName;
    private Date createdAt;

    private SubjectDTO subject;
    private List<FileEntityDTO> files;

    private List<Like> likes;

    private int likeCount;

    private List<Comment> comments;





    public PostDto() {
        this.createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }


    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public List<FileEntityDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntityDTO> files) {
        this.files = files;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
