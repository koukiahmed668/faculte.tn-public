package tn.faculte.facultebackend.Dto;

import tn.faculte.facultebackend.Entity.PaidCourse;
import tn.faculte.facultebackend.Entity.User;

import java.time.LocalDate;

public class PaidCourseEnrollmentDTO {
    private Long id;
    private User user;
    private PaidCourse paidCourse;
    private LocalDate enrollmentDate;
    private boolean paymentStatus; // true if paid, false otherwise

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaidCourse getPaidCourse() {
        return paidCourse;
    }

    public void setPaidCourse(PaidCourse paidCourse) {
        this.paidCourse = paidCourse;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
