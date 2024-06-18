package tn.faculte.facultebackend.Service.Impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Config.JwtTokenUtil;
import tn.faculte.facultebackend.Dto.LoginDto;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.*;
import tn.faculte.facultebackend.LoginResponse.LoginMesage;
import tn.faculte.facultebackend.Repo.MajorRepo;
import tn.faculte.facultebackend.Repo.SpecialtyRepo;
import tn.faculte.facultebackend.Repo.UserRepo;
import tn.faculte.facultebackend.Service.MailService.EmailService;
import tn.faculte.facultebackend.Service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserIMPL implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MajorRepo majorRepo;
    @Autowired
    private SpecialtyRepo specialtyRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserIMPL(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public String addUser(UserDto userDto, Set<Role> roles) {
        try {
            if (userRepo.existsByEmail(userDto.getEmail())) {
                return "Email is already in use. Please choose a different email address.";
            }
            String verificationToken = generateVerificationToken();


            Enrollment enrollment = new Enrollment();
            enrollment.setEnrollmentYear(userDto.getEnrollmentYear());

            Major major = majorRepo.findByName(userDto.getMajorName());
            if (major == null) {
                // Major not found, handle the error or return an appropriate response
                return "Major not found.";
            }
            Specialty specialty = specialtyRepo.findByName(userDto.getSpecialtyName());
            if (specialty != null) {
                enrollment.setSpecialty(specialty);
            } else {
                // Handle case where specialty is not found
                return "Specialty not found.";
            }
            enrollment.setSpecialty(specialty);
            enrollment.setMajor(major);
            User user = new User(
                userDto.getId(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getNumber(),
                passwordEncoder.encode(userDto.getPassword()), // Hash the password before saving
                enrollment,
                roles
            );
            enrollment.setStudent(user);
            user.setVerificationToken(verificationToken);
            userRepo.save(user);
            sendVerificationEmail(user);
            return user.getFirstName();
    } catch (DataIntegrityViolationException e) {
        // Handle the exception when a duplicate email is detected
        return "Error while saving the user.";
    }
    }
    UserDto userDto;

    @Override
    public Optional<UserDto> getUserById(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        return userOptional.map(this::convertToDto);
    }



    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            return user.getId();
        }
        return null;
    }


    @Override
    public List<User> searchUsers(String query) {
        return userRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }

    @Override
    public void sendVerificationEmail(User user) {
        String to = user.getEmail();
        String subject = "Email Verification";
        String body = "Click the following link to verify your email: http://localhost:8080/api/user/verify-email/" + user.getVerificationToken();

        emailService.sendRegistrationEmail(to, subject, body);
    }

    public void sendResetEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }

    @Override
    public boolean verifyEmail(String verificationToken) {
        User user = userRepo.findByVerificationToken(verificationToken);

        if (user != null) {
            user.setEmailVerified(true);
            userRepo.save(user);
            return true;
        }

        return false;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }





    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepo.findByEmail(email);

        if (user != null) {
            String resetToken = generateResetToken();
            user.setResetToken(resetToken);
            userRepo.save(user);

            sendResetEmail(user.getEmail(), resetToken);
        }
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }


    private void sendResetEmail(String email, String resetToken) {
        String resetLink = "http://localhost:4200/forgot-password?token=" + resetToken;
        emailService.sendResetEmail(email, "reset password", resetLink);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepo.findByResetToken(token);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null); // Clear reset token after password change
            userRepo.save(user);
            return true;
        }
        return false;
    }



    @Override
    public LoginMesage loginUser(LoginDto loginDto) {
        String msg = "";
        User user1 = userRepo.findByEmail(loginDto.getEmail());
        if (user1 != null) {
            if (!user1.isEmailVerified()) {
                return new LoginMesage("Email not verified. Please verify your email to log in.", false, null, null);
            }

            String password = loginDto.getPassword();
            String encodedPassword = user1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                // Retrieve the major ID of the user
                Long specialtyId = user1.getEnrollment().getSpecialty().getId();
                EnrollmentYear enrollmentYear = user1.getEnrollment().getEnrollmentYear();

                // Generate JWT token with major ID included
                String token = jwtTokenUtil.generateJwtToken(user1.getId(), user1.getEmail(), user1.getRolesAsString(), specialtyId,enrollmentYear);
                Optional<User> user = userRepo.findOneByEmailAndPassword(loginDto.getEmail(), encodedPassword);
                if (user.isPresent()) {
                    return new LoginMesage("Login Success", true, user.get().getRolesAsString(), token);
                } else {
                    return new LoginMesage("Login Failed", false, null, null);
                }
            } else {
                return new LoginMesage("Password Not Match", false, null, null);
            }
        } else {
            return new LoginMesage("Email not exists", false, null, null);
        }
    }



    @Override
    public String updateUser(Long userId, UserDto userDto) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setNumber(userDto.getNumber());

            // Update Enrollment Year
            if (user.getEnrollment() != null) {
                user.getEnrollment().setEnrollmentYear(userDto.getEnrollmentYear());
            } else {
                // Create new Enrollment if not exists
                Major major = majorRepo.findByName(userDto.getMajorName());
                if (major == null) {
                    return "Major not found.";
                }
                Enrollment enrollment = new Enrollment();
                enrollment.setMajor(major);
                enrollment.setEnrollmentYear(userDto.getEnrollmentYear());
                enrollment.setStudent(user);
                user.setEnrollment(enrollment);
            }

            userRepo.save(user);
            return "User updated successfully.";
        }
        return "User not found.";
    }



    @Override
    public String deleteUser(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            userRepo.deleteById(userId);
            return "User deleted successfully.";
        }
        return "User not found.";
    }

    @Override
    public List<UserDto> getStudents() {
        List<User> students = userRepo.findByRoles(Role.STUDENT);
        return convertToDtoList(students);
    }

    @Override
    public List<UserDto> getTeachers() {
        List<User> teachers = userRepo.findByRoles(Role.TEACHER);
        return convertToDtoList(teachers);
    }

    // Method to convert a List<User> to a List<UserDto>
    private List<UserDto> convertToDtoList(List<User> users) {
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setNumber(user.getNumber());
        userDto.setRoles(user.getRoles());
        userDto.setEnrollmentYear(user.getEnrollment().getEnrollmentYear());
        userDto.setMajorName(user.getEnrollment().getMajor().getName());
        userDto.setSpecialtyName(user.getEnrollment().getSpecialty().getName());
        return userDto;
    }



}
