package tn.faculte.facultebackend.Service;

import org.springframework.web.multipart.MultipartFile;
import tn.faculte.facultebackend.Entity.FileEntity;

public interface FileService {

    FileEntity storeFile(MultipartFile file);

    FileEntity getFileById(Long fileId);
}
