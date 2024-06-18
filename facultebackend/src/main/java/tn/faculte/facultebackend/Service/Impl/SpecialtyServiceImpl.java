package tn.faculte.facultebackend.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.SpecialtyDTO;
import tn.faculte.facultebackend.Entity.Major;
import tn.faculte.facultebackend.Entity.Specialty;
import tn.faculte.facultebackend.Repo.MajorRepo;
import tn.faculte.facultebackend.Repo.SpecialtyRepo;
import tn.faculte.facultebackend.Service.SpecialtyService;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    @Autowired
    private  SpecialtyRepo specialtyRepository;
    @Autowired
    private  MajorRepo majorRepository;



    @Override
    public Specialty addSpecialtyToMajor(Long majorId, SpecialtyDTO specialtyDTO) {
        Optional<Major> optionalMajor = majorRepository.findById(majorId);
        if (optionalMajor.isPresent()) {
            Major major = optionalMajor.get();
            Specialty newSpecialty = new Specialty();
            newSpecialty.setName(specialtyDTO.getName());
            newSpecialty.setMajor(major);
            return specialtyRepository.save(newSpecialty); // Save the new Specialty
        } else {
            // Handle case where Major with given ID is not found
            // You can throw an exception or handle it according to your application's logic
            return null;
        }
    }

    @Override
    public void deleteSpecialty(Long specialtyId) {
        specialtyRepository.deleteById(specialtyId);
    }

    @Override
    public Specialty updateSpecialty(Long specialtyId, Specialty updatedSpecialty) {
        Optional<Specialty> optionalSpecialty = specialtyRepository.findById(specialtyId);
        if (optionalSpecialty.isPresent()) {
            Specialty specialty = optionalSpecialty.get();
            specialty.setName(updatedSpecialty.getName());
            // Update any other fields as needed
            return specialtyRepository.save(specialty);
        } else {
            // Handle case where Specialty with given ID is not found
            // You can throw an exception or handle it according to your application's logic
            return null;
        }
    }

    @Override
    public List<Specialty> getAllSpecialtiesByMajor(Long majorId) {
        return specialtyRepository.findByMajorId(majorId);
    }
}
