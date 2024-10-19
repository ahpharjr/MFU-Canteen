package com.ONE4ALL.MFU_Canteen.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    
    private final String uploadDir = "src/main/resources/static/uploads/";

    public String storeFile(MultipartFile file){
        try{
            Path filePath = Paths.get(uploadDir + file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return file.getOriginalFilename();
        } catch (IOException e){
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
