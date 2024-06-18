package tn.faculte.facultebackend.Service;

import tn.faculte.facultebackend.Dto.MajorDTO;
import tn.faculte.facultebackend.Entity.Major;

import java.util.List;
import java.util.Optional;

public interface MajorService {
    List<Major> getAllMajors();

    Optional<Major> getMajorById(Long id);

    Major saveMajor(MajorDTO majorDTO);

    Major updateMajor(Long id, Major updatedMajor);

    void deleteMajor(Long id);

}
