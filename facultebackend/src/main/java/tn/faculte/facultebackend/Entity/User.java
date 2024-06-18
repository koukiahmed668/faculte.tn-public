package tn.faculte.facultebackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "appuser" , uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @Column(name="id", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="first_name", length = 255)
    private String firstName;
    @Column(name="last_name", length = 255)
    private String lastName;
    @Column(name="email", length = 255 , unique = true)
    private String email;
    @Column(name="number", length = 255)
    private String number;
    @Column(name="pwd", length = 255)
    private String password;

    @Column(name = "verification_token", length = 255)
    private String verificationToken;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Enrollment enrollment;
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "sharingUser", cascade = CascadeType.REMOVE)
    private List<SharedPost> sharedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @JsonBackReference // Annotation on the owning side
    private List<PaidCourse> courses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PaidCourseEnrollment> paidCourseEnrollments = new ArrayList<>();


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL) // Add this line
    private Set<Role> roles = new HashSet<>();





    public User(){}

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User(Long id, String firstName, String lastName, String email, String number, String password, Enrollment enrollment, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.password = password;
        this.roles = roles;
        this.enrollment=enrollment;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRolesAsString() {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }


    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<SharedPost> getSharedPosts() {
        return sharedPosts;
    }

    public void setSharedPosts(List<SharedPost> sharedPosts) {
        this.sharedPosts = sharedPosts;
    }

    public List<PaidCourseEnrollment> getPaidCourseEnrollments() {
        return paidCourseEnrollments;
    }

    public void setPaidCourseEnrollments(List<PaidCourseEnrollment> paidCourseEnrollments) {
        this.paidCourseEnrollments = paidCourseEnrollments;
    }

    public List<PaidCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<PaidCourse> courses) {
        this.courses = courses;
    }
}
