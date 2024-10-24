package com.ONE4ALL.MFU_Canteen.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ONE4ALL.MFU_Canteen.Entity.Picture;
import com.ONE4ALL.MFU_Canteen.Service.PictureService;

@Controller
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @GetMapping("/picture/add")
    public String showAddPictureForm(Model model) {
        model.addAttribute("picture", new Picture());
        return "add-picture";
    }

    @PostMapping("/picture/add")
    public String addPicture(@ModelAttribute Picture picture, @RequestParam("file") MultipartFile file) throws IOException {
        pictureService.savePicture(picture, file);
        return "redirect:/pictures";
    }

    @GetMapping("/pictures")
    public String listPictures(Model model) {
        model.addAttribute("pictures", pictureService.getAllPictures());
        return "picture-list";
    }

    // Show the edit form
    @GetMapping("/picture/edit/{id}")
    public String showEditPictureForm(@PathVariable("id") Long id, Model model) {
        Picture picture = pictureService.getPictureById(id);
        model.addAttribute("picture", picture);
        return "edit-picture";
    }

    // Handle the form submission for editing the picture
    @PostMapping("/picture/edit/{id}")
    public String editPicture(@PathVariable("id") Long id, @ModelAttribute Picture picture, @RequestParam("file") MultipartFile file) throws IOException {
        pictureService.updatePicture(id, picture, file);
        return "redirect:/pictures";
    }

    // Handle picture deletion
    @PostMapping("/picture/delete/{id}")
    public String deletePicture(@PathVariable("id") Long id) {
        pictureService.deletePicture(id);
        return "redirect:/pictures";
    }
}