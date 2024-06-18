package tn.faculte.facultebackend.Dto;

public class SpecialtyDTO {
    private Long id;
    private String name;
    private Long majorId;

    public SpecialtyDTO() {
    }

    public SpecialtyDTO(Long id, String name, Long majorId) {
        this.id = id;
        this.name = name;
        this.majorId = majorId;
    }

    // Getters and setters

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

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }
}

