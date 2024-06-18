package tn.faculte.facultebackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private User student;

    @ManyToOne
    @JoinColumn(name = "major_id")
    @JsonBackReference
    private Major major;

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    @JsonBackReference
    private Specialty specialty;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_year")
    private EnrollmentYear enrollmentYear;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public EnrollmentYear getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(EnrollmentYear enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }



    // Method to transition enrollment year on September 15th at a specific time (hours and minutes)
    public void transitionEnrollmentYearOnSeptember15() {
        // Create a target date and time for September 15, 2024, at 15:45
        LocalDateTime targetDateTime = LocalDateTime.of(2024, Month.SEPTEMBER, 15, 0, 1);

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Check if the current date and time is on or after the target date and time
        if (currentDateTime.isAfter(targetDateTime) || currentDateTime.isEqual(targetDateTime)) {
            // Transition the enrollment year
            switch (this.enrollmentYear) {
                case PRIMAIRE:
                    this.enrollmentYear = EnrollmentYear.SECONDAIRE;
                    break;
                case SECONDAIRE:
                    this.enrollmentYear = EnrollmentYear.TERTIAIRE;
                    break;
                default:
                    // Handle other cases if needed
                    break;
            }
        }
    }

}
