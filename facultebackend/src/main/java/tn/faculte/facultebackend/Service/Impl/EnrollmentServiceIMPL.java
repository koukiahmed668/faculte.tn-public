package tn.faculte.facultebackend.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Entity.Enrollment;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.EnrollmentRepo;
import tn.faculte.facultebackend.Repo.UserRepo;
import tn.faculte.facultebackend.Service.EnrollmentService;

import java.util.List;
@Service
public class EnrollmentServiceIMPL implements EnrollmentService {

    @Autowired
    private EnrollmentRepo enrollmentRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    @Scheduled(cron = "0 01 00 15 9 ?")
    public void transitionEnrollmentYears() {
        List<Enrollment> enrollments = enrollmentRepo.findAll();
        for (Enrollment enrollment : enrollments) {
            enrollment.transitionEnrollmentYearOnSeptember15();
            enrollmentRepo.save(enrollment); // Save the updated enrollment to the database
        }
    }

    @Override
    public Long findMajorIdByUserId(Long userId) {
        // You may want to add error handling in case the user or enrollment doesn't exist
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        Enrollment enrollment = enrollmentRepo.findByStudent(user);
        if (enrollment == null) {
            // Handle case when enrollment doesn't exist
            return null;
        }

        return enrollment.getMajor().getId();
    }
}
