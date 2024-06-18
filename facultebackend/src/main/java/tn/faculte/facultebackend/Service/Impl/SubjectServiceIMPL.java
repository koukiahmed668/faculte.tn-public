package tn.faculte.facultebackend.Service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.SubjectDTO;
import tn.faculte.facultebackend.Entity.*;
import tn.faculte.facultebackend.Repo.MajorRepo;
import tn.faculte.facultebackend.Repo.SpecialtyRepo;
import tn.faculte.facultebackend.Repo.SubjectRepo;
import tn.faculte.facultebackend.Service.SubjectService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceIMPL implements SubjectService {
    private SubjectRepo subjectRepository;
    private ModelMapper modelMapper;
    @Autowired
    private SpecialtyRepo specialtyRepo;
    @Autowired
    private MajorRepo majorRepo;
    @Autowired
    public SubjectServiceIMPL(SubjectRepo subjectRepository, ModelMapper modelMapper) {
        this.subjectRepository = subjectRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SubjectDTO convertToDto(Subject subject) {
        return modelMapper.map(subject, SubjectDTO.class);
    }


    @Override
    public List<SubjectDTO> getSubjectsBySpecialtyAndEnrollmentYear(Long specialtyId, EnrollmentYear enrollmentYear, Role userRole) {
        List<Subject> subjects;
        if (userRole == Role.TEACHER) {
            // If the user is a teacher, retrieve all subjects
            subjects = subjectRepository.findAll();
        } else {
            if (enrollmentYear == EnrollmentYear.PRIMAIRE) {
                // If the user is a student and enrolled in the primary level, retrieve primary subjects
                subjects = subjectRepository.findBySpecialtyIdAndEnrollmentYear(specialtyId, EnrollmentYear.PRIMAIRE);
            } else if (enrollmentYear == EnrollmentYear.SECONDAIRE) {
                // If the user is a student and enrolled in the secondary level, retrieve primary and secondary subjects
                subjects = subjectRepository.findBySpecialtyIdAndEnrollmentYearIn(specialtyId,
                        List.of(EnrollmentYear.PRIMAIRE, EnrollmentYear.SECONDAIRE));
            } else {
                // If the user is a student and enrolled in the tertiary level, retrieve all subjects
                subjects = subjectRepository.findBySpecialtyId(specialtyId);
            }
        }
        return subjects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<SubjectDTO> getAllSubjectsUnauthenticated() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDTO> getSubjectsBySpecialtyId(Long specialtyId) {
        // Retrieve all subjects associated with the given major ID
        List<Subject> subjects = subjectRepository.findBySpecialtyId(specialtyId);

        // Map the list of subjects to a list of SubjectDTOs
        return subjects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    @Override
    public SubjectDTO addSubject(Long majorId, Long specialtyId, SubjectDTO subjectDTO, EnrollmentYear enrollmentYear) {
        // Retrieve the major based on majorId
        Major major = majorRepo.findById(majorId)
                .orElseThrow(() -> new IllegalArgumentException("Major not found with ID: " + majorId));

        // Retrieve the specialty based on specialtyId
        Specialty specialty = specialtyRepo.findById(specialtyId)
                .orElseThrow(() -> new IllegalArgumentException("Specialty not found with ID: " + specialtyId));

        // Map SubjectDTO to Subject entity
        Subject subject = modelMapper.map(subjectDTO, Subject.class);

        // Set the major for the subject
        subject.setMajor(major);

        // Set the specialty for the subject
        subject.setSpecialty(specialty);

        // Set the enrollment year for the subject
        subject.setEnrollmentYear(enrollmentYear);

        // Save the subject
        Subject savedSubject = subjectRepository.save(subject);

        // Map the saved subject back to SubjectDTO and return
        return modelMapper.map(savedSubject, SubjectDTO.class);
    }



    @Override
    public SubjectDTO editSubject(Long subjectId, SubjectDTO subjectDTO) {
        // Retrieve the subject based on subjectId
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + subjectId));

        // Map fields from SubjectDTO to the retrieved subject entity
        subject.setName(subjectDTO.getName()); // Update other fields as needed

        // Save the updated subject
        Subject updatedSubject = subjectRepository.save(subject);

        // Map the updated subject back to SubjectDTO and return
        return modelMapper.map(updatedSubject, SubjectDTO.class);
    }
    @Override
    public void deleteSubject(Long subjectId) {
        // Check if the subject exists
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + subjectId));

        // Delete the subject
        subjectRepository.delete(subject);
    }
}
