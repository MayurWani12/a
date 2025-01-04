package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final String UPLOAD_DIR = "uploads";

    public String saveFile(MultipartFile file) {
        try {
            
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Uploads directory created at: " + uploadPath.toAbsolutePath());
            }

           
            String fileName = System.currentTimeMillis() + "_" + sanitizeFileName(file.getOriginalFilename());

           
            Path filePath = uploadPath.resolve(fileName);

          
            Files.copy(file.getInputStream(), filePath);

            System.out.println("File saved at: " + filePath.toAbsolutePath());

            return fileName; 
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + e.getMessage(), e);
        }
    }

    private String sanitizeFileName(String originalFileName) {
        if (originalFileName == null) {
            throw new IllegalArgumentException("Original file name cannot be null");
        }
        
        return originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }
}

