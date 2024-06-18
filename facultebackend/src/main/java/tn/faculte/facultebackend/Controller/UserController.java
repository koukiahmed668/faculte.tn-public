package tn.faculte.facultebackend.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Config.JwtTokenUtil;
import tn.faculte.facultebackend.Dto.LoginDto;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.Role;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.LoginResponse.LoginMesage;
import tn.faculte.facultebackend.Response.CustomResponse;
import tn.faculte.facultebackend.Service.MailService.RegistrationRequest;
import tn.faculte.facultebackend.Service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/request/teacher")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            sendEmail(registrationRequest);
            return ResponseEntity.ok("Registration request sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send registration request. Please try again later.");
        }
    }

    private void sendEmail(RegistrationRequest registrationRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("facultetnadm@gmail.com"); // Replace with your email
        message.setSubject("New Registration Request");
        message.setText("First Name: " + registrationRequest.getFirstName() +
                "\nLast Name: " + registrationRequest.getLastName() +
                "\nNumber: " + registrationRequest.getNumber() +
                "\nEmail: " + registrationRequest.getEmail());

        emailSender.send(message);
    }

    @PostMapping(path = "/save/student")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.STUDENT);

        // Call your service method to save the user
        String result = userService.addUser(userDto, roles);

        // Create a custom response object
        CustomResponse response = new CustomResponse(result);

        // Return the custom response as JSON
        return ResponseEntity.ok(response);
    }


    @GetMapping("/verify-email/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        boolean verificationResult = userService.verifyEmail(token);

        if (verificationResult) {
            return ResponseEntity.ok("Email verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification token");
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset initiated. Check your email for instructions.");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean resetSuccess = userService.resetPassword(token, newPassword);
        if (resetSuccess) {
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to reset password. Invalid token or user not found.");
        }
    }


    @PostMapping(path = "/save/admin")
    public ResponseEntity<?> saveAdmin(@RequestBody UserDto userDto) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        String result = userService.addUser(userDto, roles);
        if (result.equals("Email is already in use. Please choose a different email address.") || result.equals("Major not found.") || result.equals("Specialty not found.") || result.equals("Error while saving the user.")) {
            // Return a bad request response with the error message
            return ResponseEntity.badRequest().body(result);
        } else {
            // Return a success response with the user ID
            return ResponseEntity.ok(result);
        }
    }


    @PostMapping(path = "/save/teacher")
    public String saveTeacher(@RequestBody UserDto userDto) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.TEACHER);
        return userService.addUser(userDto, roles);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        LoginMesage loginMesage = userService.loginUser(loginDto);
        return ResponseEntity.ok(loginMesage);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        // Retrieve token from request headers
        String token = request.getHeader("Authorization");

        // Check if token is present and validate it
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            if (jwtTokenUtil.validateToken(token)) {
                return ResponseEntity.ok("Valid token");
            }
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }




    @GetMapping("/user-id")
    public ResponseEntity<Long> getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            // Assuming you have a method to retrieve the user ID from the email
            Long userId = userService.getUserIdByEmail(email);
            if (userId != null) {
                return ResponseEntity.ok(userId);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            Optional<UserDto> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        String result = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String result = userService.deleteUser(userId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/getstudents")
    public ResponseEntity<List<UserDto>> getAllStudents() {
        List<UserDto> students = userService.getStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/getteachers")
    public ResponseEntity<List<UserDto>> getAllTeachers() {
        List<UserDto> teachers = userService.getTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

}
