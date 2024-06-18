package tn.faculte.facultebackend.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.faculte.facultebackend.Entity.FileEntity;
import tn.faculte.facultebackend.Repo.FileRepo;
import tn.faculte.facultebackend.Service.FileService;
import tn.faculte.facultebackend.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceIMPL implements FileService {
    @Autowired
    private FileRepo fileRepository;

    @Override
    public FileEntity storeFile(MultipartFile file) {
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setData(file.getBytes());
            fileEntity.setCreatedAt(new Date()); // Set creation timestamp

            // Generate a unique file URL
            String fileUrl = "/files/" + UUID.randomUUID().toString(); // Or use any other mechanism to generate a unique URL

            // Set the file URL and save the fileEntity to the database
            fileEntity.setFileUrl(fileUrl);
            return fileRepository.save(fileEntity); // Return the saved FileEntity
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file", ex);
        }
    }
    @Override
    public FileEntity getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + fileId));
    }
}
