package tn.faculte.facultebackend.Dto;

import tn.faculte.facultebackend.Entity.Post;

import java.util.Date;

public class FileEntityDTO {

    private Long id;
    private String fileName;
    private String fileType;
    private byte[] data;

    private String fileUrl;
    private Date createdAt;


    public FileEntityDTO() {
    }

    public FileEntityDTO(Long id, String fileName, String fileType, byte[] data, String fileUrl, Date createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }



    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
