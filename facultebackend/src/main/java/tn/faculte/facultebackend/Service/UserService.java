package tn.faculte.facultebackend.Service;

import org.springframework.security.core.userdetails.UserDetails;
import tn.faculte.facultebackend.Dto.LoginDto;
import tn.faculte.facultebackend.Dto.UserDto;
import tn.faculte.facultebackend.Entity.Role;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.LoginResponse.LoginMesage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    String addUser(UserDto userDto , Set<Role> roles);
    LoginMesage loginUser(LoginDto loginDTO);
    void sendVerificationEmail(User user);
    boolean verifyEmail(String verificationToken);
    void initiatePasswordReset(String email);
    boolean resetPassword(String token, String newPassword);
    Optional<UserDto> getUserById(Long userId);
    Long getUserIdByEmail(String email);
    List<User> searchUsers(String query);
    String updateUser(Long userId, UserDto userDto);
    String deleteUser(Long userId);
    List<UserDto> getStudents();
    List<UserDto> getTeachers();







}
