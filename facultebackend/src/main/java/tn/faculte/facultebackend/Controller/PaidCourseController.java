package tn.faculte.facultebackend.Controller;

import com.google.api.services.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.faculte.facultebackend.Dto.FileEntityDTO;
import tn.faculte.facultebackend.Dto.PaidCourseDTO;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.FileEntity;
import tn.faculte.facultebackend.Entity.PaidCourse;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Service.FileService;
import tn.faculte.facultebackend.Service.PaidCourseEnrollmentService;
import tn.faculte.facultebackend.Service.PaidCourseService;
import tn.faculte.facultebackend.Service.UserService;
import tn.faculte.facultebackend.exception.ResourceNotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/paidcourse")
public class PaidCourseController {

   /* @Autowired
    private PaidCourseService paidCourseService;
    @Autowired
    private PaidCourseEnrollmentService paidCourseEnrollmentService;

    @Autowired
    private UserService userService;
    @Autowired
    private Calendar calendar;
    @Autowired
    private FileService fileService;

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam LocalDateTime meetingDateTime,
            @RequestParam Long teacherId,
            @RequestParam(name = "file", required = false) MultipartFile file
    ) {
        try {
            // Validate that the meeting date is not null
            if (meetingDateTime == null) {
                return ResponseEntity.badRequest().body("Meeting date cannot be null");
            }

            // Retrieve the teacher by teacherId (you might want to add validation here)
            UserDto teacher = userService.getUserById(teacherId)
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

            // Create the paid course entity
            PaidCourse paidCourse = new PaidCourse();
            paidCourse.setName(name);
            paidCourse.setDescription(description);
            paidCourse.setPrice(price);
            paidCourse.setMeetingDateTime(meetingDateTime);

            // If file is provided, handle it
            if (file != null) {
                FileEntity fileEntity = fileService.storeFile(file);
                fileEntity.setPaidCourse(paidCourse);
                paidCourse.setFile(fileEntity);
            }

            // Create the course
            PaidCourse createdCourse = paidCourseService.createCourse(paidCourse, teacher);

            // Create Google Meet event and send host link to the teacher
            paidCourseService.createGoogleMeetEvent(createdCourse, meetingDateTime, calendar);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            // Handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create course: " + e.getMessage());
        }
    }










    @GetMapping("/getallpaidcourses")
    public ResponseEntity<List<PaidCourseDTO>> getAllCourses() {
        List<PaidCourseDTO> courses = paidCourseService.getAllCourses();
        return ResponseEntity.ok().body(courses);
    }






    // Get a single paid course by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaidCourse> getCourseById(@PathVariable Long id) {
        PaidCourse course = paidCourseService.getCourseById(id);
        return ResponseEntity.ok().body(course);
    }

    // Update a paid course
    @PutMapping("/{id}")
    public ResponseEntity<PaidCourse> updateCourse(@PathVariable Long id, @RequestBody PaidCourse updatedCourse) {
        PaidCourse course = paidCourseService.updateCourse(id, updatedCourse);
        return ResponseEntity.ok().body(course);
    }

    // Delete a paid course
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        paidCourseService.deleteCourse(id);
        return ResponseEntity.ok().body("Course with ID " + id + " has been deleted successfully.");
    }


    @PostMapping("/create-proposed")
    public ResponseEntity<?> createProposedCourse(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam Long teacherId
    ) {

        UserDto teacher = userService.getUserById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

            PaidCourse paidCourse = new PaidCourse();
            paidCourse.setName(name);
            paidCourse.setDescription(description);
            paidCourse.setPrice(price);


            PaidCourse createdCourse = paidCourseService.createProposedCourse(paidCourse, teacher);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);

    }


    @GetMapping("/get-proposed")
    public ResponseEntity<List<PaidCourseDTO>> getProposedCourses() {
        List<PaidCourseDTO> proposedCourses = paidCourseService.getProposedCourses();
        return ResponseEntity.ok().body(proposedCourses);
    }*/

}