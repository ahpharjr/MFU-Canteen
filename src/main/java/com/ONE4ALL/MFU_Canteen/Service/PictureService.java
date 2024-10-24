package com.ONE4ALL.MFU_Canteen.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ONE4ALL.MFU_Canteen.Entity.Picture;
import com.ONE4ALL.MFU_Canteen.Repository.PictureRepository;

@Service
public class PictureService {

    private final String uploadDir = "uploads/";

    @Autowired
    private PictureRepository pictureRepository;

    public void savePicture(Picture picture, MultipartFile file) throws IOException {
        // Save file to the server
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());

        // Save picture data in the database
        picture.setFileName(fileName);
        pictureRepository.save(picture);
    }

    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    public Picture getPictureById(Long id){
        return pictureRepository.findById(id).orElse(null);
    }

    public void updatePicture(Long id, Picture updatedPicture, MultipartFile file) throws IOException {
        Picture picture = pictureRepository.findById(id).orElseThrow(() -> new RuntimeException("Picture not found"));

        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
            picture.setFileName(fileName);  // Update the file name
        }

        picture.setPictureName(updatedPicture.getPictureName());
        pictureRepository.save(picture);
    }

    public void deletePicture(Long id) {
        pictureRepository.deleteById(id);
    }
}

