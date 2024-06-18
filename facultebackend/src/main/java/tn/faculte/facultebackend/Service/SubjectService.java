package tn.faculte.facultebackend.Service;

import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.SubjectDTO;
import tn.faculte.facultebackend.Entity.EnrollmentYear;
import tn.faculte.facultebackend.Entity.Role;
import tn.faculte.facultebackend.Entity.Subject;

import java.util.List;

public interface SubjectService {

    List<SubjectDTO> getAllSubjects();
    List<SubjectDTO> getSubjectsBySpecialtyAndEnrollmentYear(Long specialtyId, EnrollmentYear enrollmentYear, Role userRole);
    public SubjectDTO addSubject(Long majorId, Long specialtyId, SubjectDTO subjectDTO, EnrollmentYear enrollmentYear);
    SubjectDTO editSubject(Long subjectId, SubjectDTO subjectDTO);
    void deleteSubject(Long subjectId);
    List<SubjectDTO> getAllSubjectsUnauthenticated();
    List<SubjectDTO> getSubjectsBySpecialtyId(Long specialtyId);

}
