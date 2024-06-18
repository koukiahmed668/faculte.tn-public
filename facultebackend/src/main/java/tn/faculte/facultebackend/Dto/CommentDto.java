package tn.faculte.facultebackend.Dto;

import tn.faculte.facultebackend.Entity.Post;
import tn.faculte.facultebackend.Entity.User;

public class CommentDto {
    private Long id;


    private UserDto user;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
