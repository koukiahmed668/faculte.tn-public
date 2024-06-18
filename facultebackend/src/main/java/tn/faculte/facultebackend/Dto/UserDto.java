package tn.faculte.facultebackend.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import tn.faculte.facultebackend.Entity.Enrollment;
import tn.faculte.facultebackend.Entity.EnrollmentYear;
import tn.faculte.facultebackend.Entity.Role;

import java.util.HashSet;
import java.util.Set;

public class UserDto {

    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private String password;

    private byte[] avatar;

    private String verificationToken;
    private String resetToken;

    private boolean emailVerified;
    private Set<Role> roles = new HashSet<>();
    @JsonIgnore

    private EnrollmentDTO enrollmentDTO;

    private EnrollmentYear enrollmentYear;

    private String majorName;
    private String specialtyName;


    public UserDto(){}


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserDto(Long id, String firstName, String lastName, String email, String number, String password, byte[] avatar, String verificationToken, String resetToken, boolean emailVerified, Set<Role> roles, EnrollmentDTO enrollmentDTO, EnrollmentYear enrollmentYear, String majorName, String specialtyName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.password = password;
        this.avatar = avatar;
        this.verificationToken = verificationToken;
        this.resetToken = resetToken;
        this.emailVerified = emailVerified;
        this.roles = roles;
        this.enrollmentDTO = enrollmentDTO;
        this.enrollmentYear = enrollmentYear;
        this.majorName = majorName;
        this.specialtyName = specialtyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public EnrollmentDTO getEnrollmentDTO() {
        return enrollmentDTO;
    }

    public void setEnrollmentDTO(EnrollmentDTO enrollmentDTO) {
        this.enrollmentDTO = enrollmentDTO;
    }

    public EnrollmentYear getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(EnrollmentYear enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
