package tn.faculte.facultebackend.Dto;

import java.util.List;

public class MajorDTO {
    private Long id;
    private String name;
    private List<SubjectDTO> subjects;

    public MajorDTO(Long id, String name, List<SubjectDTO> subjects) {
        this.id = id;
        this.name = name;
        this.subjects = subjects;
    }

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

    public List<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDTO> subjects) {
        this.subjects = subjects;
    }
}
