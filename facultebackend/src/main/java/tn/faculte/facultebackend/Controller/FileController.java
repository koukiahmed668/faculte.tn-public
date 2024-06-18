package tn.faculte.facultebackend.Controller;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.faculte.facultebackend.Entity.FileEntity;
import tn.faculte.facultebackend.Service.FileService;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files/{fileId}/blob")
    public ResponseEntity<byte[]> downloadFileBlob(@PathVariable Long fileId) {
        // Retrieve file entity from the database using fileId
        FileEntity fileEntity = fileService.getFileById(fileId);

        // Set the appropriate Content-Type header based on the file's MIME type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileEntity.getFileType()));

        // Set the Content-Disposition header to inline
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(fileEntity.getFileName())
                .build());

        // Return the file data as a byte array
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileEntity.getData());
    }


}
