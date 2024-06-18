package tn.faculte.facultebackend.Controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.faculte.facultebackend.Dto.SpecialtyDTO;
import tn.faculte.facultebackend.Entity.Specialty;
import tn.faculte.facultebackend.Service.SpecialtyService;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {
    @Autowired
    private SpecialtyService specialtyService;


    @PostMapping("/add/{majorId}")
    public ResponseEntity<Specialty> addSpecialtyToMajor(@PathVariable Long majorId, @RequestBody SpecialtyDTO specialtyDTO) {
        Specialty createdSpecialty = specialtyService.addSpecialtyToMajor(majorId, specialtyDTO);
        if (createdSpecialty != null) {
            return new ResponseEntity<>(createdSpecialty, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{specialtyId}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long specialtyId) {
        specialtyService.deleteSpecialty(specialtyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{specialtyId}")
    public ResponseEntity<Specialty> updateSpecialty(@PathVariable Long specialtyId, @RequestBody Specialty updatedSpecialty) {
        Specialty updated = specialtyService.updateSpecialty(specialtyId, updatedSpecialty);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/major/{majorId}")
    public ResponseEntity<List<Specialty>> getAllSpecialtiesByMajor(@PathVariable Long majorId) {
        List<Specialty> specialties = specialtyService.getAllSpecialtiesByMajor(majorId);
        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }
}
