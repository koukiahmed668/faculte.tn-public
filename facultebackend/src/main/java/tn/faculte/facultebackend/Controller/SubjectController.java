package tn.faculte.facultebackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Config.JwtTokenUtil;
import tn.faculte.facultebackend.Dto.SubjectDTO;
import tn.faculte.facultebackend.Entity.EnrollmentYear;
import tn.faculte.facultebackend.Entity.Role;
import tn.faculte.facultebackend.Entity.Subject;
import tn.faculte.facultebackend.Service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects(@RequestHeader("Authorization") String token) {
        Long specialtyId = jwtTokenUtil.getSpecialityIdFromToken(token.substring(7));
        EnrollmentYear enrollmentYear = jwtTokenUtil.getEnrollmentYearFromToken(token.substring(7));
        Role userRole = jwtTokenUtil.getRoleFromToken(token.substring(7)); // Get the user's role
        if (specialtyId == null || enrollmentYear == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Return unauthorized status
        }
        List<SubjectDTO> subjects = subjectService.getSubjectsBySpecialtyAndEnrollmentYear(specialtyId, enrollmentYear, userRole);
        return ResponseEntity.ok(subjects); // Return subjects with OK status
    }

    @GetMapping("/getalladmin")
    public ResponseEntity<List<SubjectDTO>> getAllSubjectsUnauthenticated() {
        List<SubjectDTO> subjects = subjectService.getAllSubjectsUnauthenticated();
        return ResponseEntity.ok(subjects); // Return subjects with OK status
    }

    @GetMapping("/subjects/{specialtyId}")
    public List<SubjectDTO> getSubjectsBySpecialtyId(@PathVariable Long specialtyId) {
        return subjectService.getSubjectsBySpecialtyId(specialtyId);
    }

    @PostMapping("/{majorId}/{specialtyId}/{enrollmentYear}")
    public ResponseEntity<SubjectDTO> addSubject(@PathVariable Long majorId,
                                                 @PathVariable Long specialtyId,
                                                 @PathVariable EnrollmentYear enrollmentYear,
                                                 @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO addedSubject = subjectService.addSubject(majorId,specialtyId, subjectDTO, enrollmentYear);
        return new ResponseEntity<>(addedSubject, HttpStatus.CREATED);
    }


    // Endpoint to edit subject
    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectDTO> editSubject(@PathVariable Long subjectId, @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO updatedSubject = subjectService.editSubject(subjectId, subjectDTO);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
    }

    // Endpoint to delete subject
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
