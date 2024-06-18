package tn.faculte.facultebackend.Service;

import tn.faculte.facultebackend.Dto.SpecialtyDTO;
import tn.faculte.facultebackend.Entity.Major;
import tn.faculte.facultebackend.Entity.Specialty;

import java.util.List;

public interface SpecialtyService {
    public Specialty addSpecialtyToMajor(Long majorId, SpecialtyDTO specialtyDTO);
    void deleteSpecialty(Long specialtyId);

    Specialty updateSpecialty(Long specialtyId, Specialty updatedSpecialty);

    List<Specialty> getAllSpecialtiesByMajor(Long majorId);
}
