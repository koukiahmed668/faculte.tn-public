package tn.faculte.facultebackend.Service.Impl;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.FileEntityDTO;
import tn.faculte.facultebackend.Dto.PaidCourseDTO;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.CourseStatus;
import tn.faculte.facultebackend.Entity.PaidCourse;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.PaidCourseRepo;
import tn.faculte.facultebackend.Service.PaidCourseService;
import tn.faculte.facultebackend.exception.ResourceNotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaidCourseServiceIMPL implements PaidCourseService {
    /*

    @Autowired
    private PaidCourseRepo paidCourseRepo;
    private final Calendar googleCalendar;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public PaidCourseServiceIMPL(PaidCourseRepo paidCourseRepo, Calendar googleCalendar) {
        this.paidCourseRepo = paidCourseRepo;
        this.googleCalendar = googleCalendar;
    }


    @Override
    public PaidCourse createCourse(PaidCourse paidCourse, UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);

        paidCourse.setStatus(CourseStatus.CREATED);
        paidCourse.setTeacher(user);

        try {
            // Create Google Meet event and retrieve conference link
            String conferenceLink = createGoogleMeetEvent(paidCourse, paidCourse.getMeetingDateTime(), googleCalendar);

            // Associate the conference link with the course entity
            paidCourse.setConferenceLink(conferenceLink);

            // Send host link to teacher
            sendHostLinkToTeacher(user.getEmail(), conferenceLink);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }

        return paidCourseRepo.save(paidCourse);
    }



    @Override
    public String createGoogleMeetEvent(PaidCourse paidCourse, LocalDateTime dateTime, Calendar calendar) throws IOException {
        // Create a Google Meet event
        if (dateTime == null) {
            // Handle the case where the date is null
            throw new IllegalArgumentException("Date cannot be null");
        }

        // Define a DateTimeFormatter for the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        Event event = new Event()
                .setSummary(paidCourse.getName() + " - Online Meeting")
                .setDescription("Join the online meeting for the course: " + paidCourse.getName());

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(dateTime.format(formatter))) // Use the formatted string directly
                .setTimeZone("UTC"); // Set the desired timezone
        event.setStart(start);

        // Calculate the end time as, for example, one hour after the start time
        LocalDateTime endTime = dateTime.plusHours(1); // Adjust this as per your requirement
        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endTime.format(formatter))) // Use the formatted end time string
                .setTimeZone("UTC"); // Set the desired timezone
        event.setEnd(end);

        // Create a conference for the event
        ConferenceData conferenceData = new ConferenceData();
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
        createConferenceRequest.setRequestId(UUID.randomUUID().toString()); // Set a unique request ID
        conferenceData.setCreateRequest(createConferenceRequest);
        event.setConferenceData(conferenceData);

        // Insert event into the calendar
        try {
            Event createdEvent = calendar.events().insert("primary", event)
                    .setConferenceDataVersion(1) // Set the conference data version
                    .execute();
            System.out.println("Event creation response: " + createdEvent.toPrettyString());

            // Retrieve the conference link from the event if available
            if (createdEvent.getConferenceData() != null && createdEvent.getConferenceData().getEntryPoints() != null && !createdEvent.getConferenceData().getEntryPoints().isEmpty()) {
                String conferenceLink = createdEvent.getConferenceData().getEntryPoints().get(0).getUri();
                System.out.println("Conference Link: " + conferenceLink); // Log the conference link

                // Return the conference link
                return conferenceLink;
            } else {
                System.out.println("No conference link available.");
                return null;
            }

        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
            throw e;
        }
    }



    public void sendHostLinkToTeacher(String teacherEmail, String conferenceLink) {
        // Send the host link to the teacher's email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(teacherEmail);
            message.setSubject("Google Meet Link for Your Course");
            message.setText("Dear Teacher,\n\nHere is the Google Meet link for your course: " + conferenceLink);

            javaMailSender.send(message);
            System.out.println("Host link sent to teacher: " + teacherEmail);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
    private void sendEmail(String to, String subject, String text) throws MessagingException {
        // Compose the email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // Send the email
        javaMailSender.send(message);
    }


    @Override
    public List<PaidCourseDTO> getAllCourses() {
        List<PaidCourse> courses = paidCourseRepo.findAll();
        List<PaidCourseDTO> courseDTOs = new ArrayList<>();

        for (PaidCourse course : courses) {
            PaidCourseDTO courseDTO = new PaidCourseDTO();
            courseDTO.setId(course.getId());
            courseDTO.setName(course.getName());
            courseDTO.setDescription(course.getDescription());
            courseDTO.setPrice(course.getPrice());
            courseDTO.setTeacherId(course.getTeacher().getId()); // Populate teacherId
            courseDTO.setStatus(course.getStatus());

            // Check if the course has an associated file
            if (course.getFile() != null) {
                // Create FileEntityDTO and set its attributes
                FileEntityDTO fileDTO = new FileEntityDTO();
                fileDTO.setId(course.getFile().getId());
                fileDTO.setFileName(course.getFile().getFileName());
                fileDTO.setFileType(course.getFile().getFileType());
                fileDTO.setFileUrl(course.getFile().getFileUrl());
                fileDTO.setCreatedAt(course.getFile().getCreatedAt());

                // Set the FileEntityDTO in the PaidCourseDTO
                courseDTO.setFile(fileDTO);
            }

            courseDTOs.add(courseDTO);
        }

        return courseDTOs;
    }


    @Override
    public PaidCourse getCourseById(Long paidCourseId) {
        return paidCourseRepo.findById(paidCourseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + paidCourseId));
    }

    @Override
    public PaidCourse createProposedCourse(PaidCourse paidCourse, UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);

        // Set the status of the course to "proposed"
        paidCourse.setStatus(CourseStatus.PROPOSED);

        // Set the teacher of the course
        paidCourse.setTeacher(user);

        // Save the proposed course
        return paidCourseRepo.save(paidCourse);
    }

    @Override
    public List<PaidCourseDTO> getProposedCourses() {
        List<PaidCourse> proposedCourses = paidCourseRepo.findByStatus(CourseStatus.PROPOSED);
        return proposedCourses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PaidCourseDTO convertToDTO(PaidCourse course) {
        PaidCourseDTO courseDTO = new PaidCourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setTeacherId(course.getTeacher().getId());
        courseDTO.setStatus(course.getStatus());

        return courseDTO;
    }




    @Override
    public PaidCourse updateCourse(Long courseId, PaidCourse updatedCourse) {
        PaidCourse existingCourse = paidCourseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Update fields
        existingCourse.setName(updatedCourse.getName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setPrice(updatedCourse.getPrice());

        // Save and return updated course
        return paidCourseRepo.save(existingCourse);
    }

    @Override
    public void deleteCourse(Long courseId) {
        PaidCourse course = paidCourseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        paidCourseRepo.delete(course);
    }

     */
}