package tn.faculte.facultebackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "sharedpost")
public class SharedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "original_post_id")
    @JsonBackReference
    private Post originalPost;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_user_id")
    @JsonBackReference
    private User sharingUser;

    @Column(name = "additional_content")
    private String additionalContent;

    @Column(name = "created_at")
    private Date createdAt;

    public SharedPost() {
    }

    public SharedPost(Long id, Post originalPost, User sharingUser, String additionalContent, Date createdAt) {
        this.id = id;
        this.originalPost = originalPost;
        this.sharingUser = sharingUser;
        this.additionalContent = additionalContent;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getOriginalPost() {
        return originalPost;
    }

    public void setOriginalPost(Post originalPost) {
        this.originalPost = originalPost;
    }

    public User getSharingUser() {
        return sharingUser;
    }

    public void setSharingUser(User sharingUser) {
        this.sharingUser = sharingUser;
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
