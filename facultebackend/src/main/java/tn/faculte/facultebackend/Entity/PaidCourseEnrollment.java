package tn.faculte.facultebackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "course_enrollment")
public class PaidCourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private PaidCourse paidCourse;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "payment_status")
    private boolean paymentStatus; // true if paid, false otherwise

    public PaidCourseEnrollment() {
    }

    public PaidCourseEnrollment(Long id, User user, PaidCourse paidCourse, LocalDate enrollmentDate, boolean paymentStatus) {
        this.id = id;
        this.user = user;
        this.paidCourse = paidCourse;
        this.enrollmentDate = enrollmentDate;
        this.paymentStatus = paymentStatus;
    }

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
