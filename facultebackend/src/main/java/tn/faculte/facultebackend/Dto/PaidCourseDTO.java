package tn.faculte.facultebackend.Dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import tn.faculte.facultebackend.Entity.CourseStatus;
import tn.faculte.facultebackend.Entity.FileEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaidCourseDTO {

    private Long id;
    private String name;
    private String description;
    private double price;
    private Long teacherId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)

    private LocalDateTime meetingDateTime;

    private String conferenceLink;

    private FileEntityDTO file;
    private CourseStatus status;



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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
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


    public FileEntityDTO getFile() {
        return file;
    }

    public void setFile(FileEntityDTO file) {
        this.file = file;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }
}