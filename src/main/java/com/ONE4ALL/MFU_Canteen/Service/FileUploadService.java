package com.ONE4ALL.MFU_Canteen.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileUploadService {

    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/items/";

    public String uploadFile(MultipartFile file) throws IOException {
        // Ensure directory exists
        Files.createDirectories(Paths.get(uploadDir));

        // Generate unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        // Save the file
        file.transferTo(filePath.toFile());

        // Return the relative URL path
        return "/uploads/items/" + fileName;
    }
}
