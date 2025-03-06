package com.udacity.jwdnd.course1.cloudstorage.model;

public class File {
    Integer fileId;
    String filename;
    String fileSize;

    String contentType;
    Integer userId;
    byte[] fileData;

    public File(Integer fileId, String filename, String contentType, String fileSize, Integer userId, byte[] fileData) {
        this.fileId = fileId;
        this.filename = filename;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.userId = userId;
        this.fileData = fileData;
    }

    public File(){}

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}