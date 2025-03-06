package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }

    public int save(MultipartFile file, Integer userId) throws IOException {
        String fileName = file.getOriginalFilename();
        File existingFile = fileMapper.findByFilename(fileName);
        if (existingFile != null) {
            throw new IOException("File with the same name already exists.");
        }

        File fileEntity = new File();
        fileEntity.setFileData(file.getBytes());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setFileSize(file.getSize() + "");
        fileEntity.setFilename(file.getOriginalFilename());
        fileEntity.setUserId(userId);
        return fileMapper.save(fileEntity);
    }

    public File getFileById(Integer id) {
        return fileMapper.findById(id);
    }

    public void delete(Integer id){
        fileMapper.delete(id);
    }
}
