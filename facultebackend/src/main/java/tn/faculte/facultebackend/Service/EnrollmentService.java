package tn.faculte.facultebackend.Service;

public interface EnrollmentService {
    void transitionEnrollmentYears();
    Long findMajorIdByUserId(Long userId);

}
