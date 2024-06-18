package tn.faculte.facultebackend.Dto;

import tn.faculte.facultebackend.Entity.Enrollment;
import tn.faculte.facultebackend.Entity.EnrollmentYear;
import tn.faculte.facultebackend.Entity.Major;
import tn.faculte.facultebackend.Entity.Specialty;
import tn.faculte.facultebackend.Entity.User;

public class EnrollmentDTO {

    private Long id;
    private UserDto studentId;
    private MajorDTO major;
    private Specialty specialty;
    private EnrollmentYear enrollmentYear;

    public EnrollmentDTO() {
        // Default constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getStudentId() {
        return studentId;
    }

    public void setStudentId(UserDto studentId) {
        this.studentId = studentId;
    }

    public MajorDTO getMajor() {
        return major;
    }

    public void setMajor(MajorDTO major) {
        this.major = major;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public EnrollmentYear getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(EnrollmentYear enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }
}