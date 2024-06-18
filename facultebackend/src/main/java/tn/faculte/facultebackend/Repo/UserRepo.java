package tn.faculte.facultebackend.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.faculte.facultebackend.Entity.Role;
import tn.faculte.facultebackend.Entity.User;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findById(Long id); // Change the return type to Optional<User>

    User findByEmail(String email);
    User findByVerificationToken(String verificationToken);

    Optional<User> findOneByEmailAndPassword(String email, String encodedPassword);

    // New method to check if an email exists
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = ?1")
    boolean existsByEmail(String email);

    User findByResetToken(String resetToken);


    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String query, String query1);

    List<User> findByRoles(Role role);
}
