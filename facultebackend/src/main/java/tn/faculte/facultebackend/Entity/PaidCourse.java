package tn.faculte.facultebackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "paidcourse")
public class PaidCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @JsonBackReference // Annotation on the non-owning side
    private User teacher;

    @Column(name = "meeting_date")
    private LocalDateTime meetingDateTime;

    @Column(length = 1000) // Adjust length as needed
    private String conferenceLink;
    @OneToOne(mappedBy = "paidCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private FileEntity file;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CourseStatus status;


    public PaidCourse() {
    }

    public PaidCourse(Long id, String name, String description, double price, User teacher, LocalDateTime meetingDateTime, String conferenceLink, FileEntity file, CourseStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.teacher = teacher;
        this.meetingDateTime = meetingDateTime;
        this.conferenceLink = conferenceLink;
        this.file = file;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(LocalDateTime meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }

    public String getConferenceLink() {
        return conferenceLink;
    }

    public void setConferenceLink(String conferenceLink) {
        this.conferenceLink = conferenceLink;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }
}