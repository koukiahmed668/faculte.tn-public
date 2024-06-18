package tn.faculte.facultebackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Dto.MajorDTO;
import tn.faculte.facultebackend.Entity.Major;
import tn.faculte.facultebackend.Service.EnrollmentService;
import tn.faculte.facultebackend.Service.MajorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/major")
public class MajorController {

    @Autowired
    private MajorService majorService;
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/majors")
    public List<Major> getAllMajors() {
        return majorService.getAllMajors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Major> getMajorById(@PathVariable Long id) {
        Optional<Major> major = majorService.getMajorById(id);
        return major.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addmajor")
    public ResponseEntity<Major> addMajor(@RequestBody MajorDTO major) {
        Major savedMajor = majorService.saveMajor(major);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMajor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Major> updateMajor(@PathVariable Long id, @RequestBody Major major) {
        Major updatedMajor = majorService.updateMajor(id, major);
        if (updatedMajor != null) {
            return ResponseEntity.ok(updatedMajor);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable Long id) {
        majorService.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public Long getMajorIdByUserId(@PathVariable Long userId) {
        return enrollmentService.findMajorIdByUserId(userId);
    }

}
