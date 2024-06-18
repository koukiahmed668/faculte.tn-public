package tn.faculte.facultebackend.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.PaidCourse;
import tn.faculte.facultebackend.Entity.PaidCourseEnrollment;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Service.PaidCourseEnrollmentService;
import tn.faculte.facultebackend.Service.PaidCourseService;
import tn.faculte.facultebackend.Service.UserService;
import tn.faculte.facultebackend.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/paidcourseenrollment")
public class PaidCourseEnrollmentController {

   /* @Autowired
    private PaidCourseEnrollmentService paidCourseEnrollmentService;
    @Autowired
    private PaidCourseService paidCourseService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<PaidCourseEnrollment> enrollUserInCourse(@PathVariable Long courseId, @RequestParam Long userId) {
        // Retrieve the course by courseId (you might want to add validation here)
        PaidCourse paidCourse = paidCourseService.getCourseById(courseId);

        // Enroll the user in the course
        PaidCourseEnrollment enrollment = paidCourseEnrollmentService.enrollUserInCourse(userId, paidCourse);

        // Return the enrollment details (unpaid)
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }



    @PostMapping("/confirm-payment/{enrollmentId}")
    public ResponseEntity<String> confirmPayment(@PathVariable Long enrollmentId) {
        paidCourseEnrollmentService.confirmPayment(enrollmentId);
        return ResponseEntity.ok().body("Payment confirmed for enrollment with ID: " + enrollmentId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaidCourseEnrollment>> getUserEnrollments(@PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + userId));

        List<PaidCourseEnrollment> userEnrollments = paidCourseEnrollmentService.getUserEnrollments(userDto);
        return ResponseEntity.ok().body(userEnrollments);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<PaidCourseEnrollment>> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        List<PaidCourseEnrollment> courseEnrollments = paidCourseEnrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok().body(courseEnrollments);
    }


    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable Long enrollmentId) {
        paidCourseEnrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok().body("Enrollment with ID " + enrollmentId + " has been deleted successfully.");
    }*/
}
