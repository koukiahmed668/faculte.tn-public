package tn.faculte.facultebackend.Service.Impl;


import jakarta.mail.MessagingException;
import com.google.api.services.calendar.Calendar;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.PaidCourse;
import tn.faculte.facultebackend.Entity.PaidCourseEnrollment;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.PaidCourseEnrollmentRepo;
import tn.faculte.facultebackend.Service.PaidCourseEnrollmentService;
import tn.faculte.facultebackend.Service.PaidCourseService;
import tn.faculte.facultebackend.Service.UserService;
import tn.faculte.facultebackend.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaidCourseEnrollmentServiceIMPL implements PaidCourseEnrollmentService {
    /*
    @Autowired
    private PaidCourseEnrollmentRepo paidCourseEnrollmentRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PaidCourseService paidCourseService;
    @Autowired
    private Calendar googleCalendar;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaidCourseEnrollment enrollUserInCourse(Long userId, PaidCourse paidCourse) {
        Optional<UserDto> userOptional = userService.getUserById(userId);
        UserDto userDto = userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Enroll the user in the course
        PaidCourseEnrollment enrollment = new PaidCourseEnrollment();
        enrollment.setPaidCourse(paidCourse);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setPaymentStatus(false); // Initially not paid

        // Save the enrollment
        PaidCourseEnrollment savedEnrollment = paidCourseEnrollmentRepo.save(enrollment);

        try {
            // Retrieve the conference link associated with the course
            String conferenceLink = paidCourse.getConferenceLink();

            // Send conference link to the user
            sendConferenceLinkToUser(userDto.getEmail(), conferenceLink);

        } catch (MessagingException e) {
            // Handle exception
            e.printStackTrace();
        }

        return savedEnrollment;
    }


    private void sendConferenceLinkToUser(String userEmail, String conferenceLink) throws MessagingException {
        // Send the conference link to the user's email
        String subject = "Conference Link for Your Course";
        String text = "Dear User,\n\nHere is the conference link for your course: " + conferenceLink;
        sendEmail(userEmail, subject, text);
    }


    @Override
    public void confirmPayment(Long userId) {
        // Find all enrollments for the user
        List<PaidCourseEnrollment> userEnrollments = paidCourseEnrollmentRepo.findByUserId(userId);

        // Simulate confirming payment for all enrollments
        for (PaidCourseEnrollment enrollment : userEnrollments) {
            enrollment.setPaymentStatus(true);
            paidCourseEnrollmentRepo.save(enrollment);
        }
    }

    @Override
    public void updateEnrollment(PaidCourseEnrollment enrollment) {
        // Perform any necessary validation or business logic checks
        paidCourseEnrollmentRepo.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Long enrollmentId) {
        PaidCourseEnrollment enrollment = paidCourseEnrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

        paidCourseEnrollmentRepo.delete(enrollment);
    }

    @Override
    public List<PaidCourseEnrollment> getUserEnrollments(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        return paidCourseEnrollmentRepo.findByUser(user);
    }

    @Override
    public List<PaidCourseEnrollment> getEnrollmentsByCourseId(Long courseId) {
        return paidCourseEnrollmentRepo.findByPaidCourseId(courseId);
    }




    // Helper method to send an email
    private void sendEmail(String to, String subject, String text) throws MessagingException {
        // Compose the email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // Send the email
        javaMailSender.send(message);
    }

*/
}
