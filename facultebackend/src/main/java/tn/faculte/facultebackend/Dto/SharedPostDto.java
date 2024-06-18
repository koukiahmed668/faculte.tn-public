package tn.faculte.facultebackend.Dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Entity.User;

import java.util.Date;

public class SharedPostDto {
    private Long id;
    private Post post;
    private User user;
    private String additionalContent;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAdditionalContent() {
        return additionalContent;
    }

    public void setAdditionalContent(String additionalContent) {
        this.additionalContent = additionalContent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
