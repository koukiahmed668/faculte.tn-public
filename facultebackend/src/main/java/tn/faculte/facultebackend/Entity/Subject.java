package tn.faculte.facultebackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Table(name = "Subject")
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "subject")
    private List<Post> posts;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_year")
    private EnrollmentYear enrollmentYear;

    @ManyToOne
    @JoinColumn(name = "major_id")
    @JsonBackReference
    private Major major;

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    @JsonBackReference
    private Specialty specialty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public EnrollmentYear getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(EnrollmentYear enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }
}
