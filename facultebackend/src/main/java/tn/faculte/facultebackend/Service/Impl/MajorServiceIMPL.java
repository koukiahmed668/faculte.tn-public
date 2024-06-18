package tn.faculte.facultebackend.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.faculte.facultebackend.Dto.MajorDTO;
import tn.faculte.facultebackend.Entity.Major;
import tn.faculte.facultebackend.Repo.MajorRepo;
import tn.faculte.facultebackend.Service.MajorService;
import java.util.List;
import java.util.Optional;

@Service
public class MajorServiceIMPL implements MajorService {
    @Autowired
    private MajorRepo majorRepo;
    @Override
    public List<Major> getAllMajors() {
        return majorRepo.findAll();
    }
    @Override
    public Optional<Major> getMajorById(Long id) {
        return majorRepo.findById(id);
    }
    @Override
    public Major saveMajor(MajorDTO majorDTO) {
        Major major = new Major();
        major.setName(majorDTO.getName());
        return majorRepo.save(major);
    }
    @Override
    public Major updateMajor(Long id, Major updatedMajor) {
        Optional<Major> existingMajorOptional = majorRepo.findById(id);
        if (existingMajorOptional.isPresent()) {
            Major existingMajor = existingMajorOptional.get();
            existingMajor.setName(updatedMajor.getName());
            return majorRepo.save(existingMajor);
        }
        return null; // Major not found
    }
    @Override
    public void deleteMajor(Long id) {
        majorRepo.deleteById(id);
    }


}
