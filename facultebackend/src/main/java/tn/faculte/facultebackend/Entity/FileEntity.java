package tn.faculte.facultebackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table (name = "file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "data", columnDefinition = "BYTEA")
    private byte[] data;

    @Column(name = "file_type")
    private String fileType;
    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    @OneToOne
    @JoinColumn(name = "paid_course_id")
    @JsonBackReference
    private PaidCourse paidCourse;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    public FileEntity() {
    }
    public FileEntity(String fileName, byte[] data, String fileType, String fileUrl, Date createdAt) {
        this.fileName = fileName;
        this.data = data;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PaidCourse getPaidCourse() {
        return paidCourse;
    }

    public void setPaidCourse(PaidCourse paidCourse) {
        this.paidCourse = paidCourse;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
