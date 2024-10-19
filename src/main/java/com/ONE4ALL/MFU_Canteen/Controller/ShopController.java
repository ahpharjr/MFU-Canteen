package com.ONE4ALL.MFU_Canteen.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ONE4ALL.MFU_Canteen.Entity.Canteen;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.CanteenRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;
import com.ONE4ALL.MFU_Canteen.Service.FileStorageService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;

@Controller
public class ShopController {
    
    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private CanteenRepository canteenRepo;

    @Autowired
    private ShopService shopService;

    @Autowired
    private FileStorageService fileStorageService;

    // @Value("${file.upload-dir}")
    // private String uploadDir;


    @GetMapping("/shop/add/{canteenId}")
    public String showShopForm(@PathVariable("canteenId") Long canteenId, Model model){
        Optional<Canteen> canteenOpt= canteenRepo.findById(canteenId);
        if(canteenOpt.isPresent()){
            Shop shop = new Shop();
            shop.setCanteen(canteenOpt.get());
            model.addAttribute("shop", shop);
            model.addAttribute("canteenId", canteenId);
            return "add-shop";
        }else{
            return "redirect:/canteens";
        }
    }

    // @PostMapping("/shop/add/{canteenId}")
    // public String addShop(@PathVariable Long canteenId, 
    //                       @ModelAttribute Shop shop,
    //                       @RequestParam("pictureFile") MultipartFile pictureFile,
    //                       RedirectAttributes redirectAttributes){
    
    //     System.out.println("-------------------------1-----------------------");
    
    //     Optional<Canteen> canteenOpt = canteenRepo.findById(canteenId);
    //     if (canteenOpt.isPresent()) {
    //         System.out.println("-------------------------2-----------------------");
    //         if (!pictureFile.isEmpty()) {
    //             System.out.println("-------------------------3-----------------------");
    //             try {
    //                 // Set the external directory where the files will be saved
    //                 // String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath();
    //                 String uploadDir = new File("C:/uploads/").getAbsolutePath();  // External directory path
    
    //                 File uploadDirFile = new File(uploadDir);
    
    //                 // Ensure the directory exists, if not create it
    //                 if (!uploadDirFile.exists()) {
    //                     uploadDirFile.mkdirs();  // Create the directory if it doesn't exist
    //                 }
    
    //                 String fileName = pictureFile.getOriginalFilename();
    //                 File uploadFile = new File(uploadDirFile, fileName);  // Create the full path for the file
    
    //                 System.out.println("-------------------------4-----------------------");
    //                 pictureFile.transferTo(uploadFile);  // Transfer the uploaded file to the specified directory
    
    //                 // Log the absolute path for debugging
    //                 System.out.println(uploadFile);
    
    //                 // Save only the relative path or filename in the database
    //                 shop.setPicture("/uploads/"+fileName);  // Save only the file name, not the full path
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //                 redirectAttributes.addFlashAttribute("errorMessages", "File upload failed.");
    //                 return "redirect:/shop/add/" + canteenId;
    //             }
    //         }
    
    //         System.out.println("-------------------------5-----------------------");
    //         shop.setCanteen(canteenOpt.get());
    //         shopRepo.save(shop);
    
    //         return "redirect:/canteen/shops/" + canteenId;
    //     } else {
    //         return "redirect:/canteens";
    //     }
    // }
    
    

    @PostMapping("/shop/add/{canteenId}")
    public String addShop(@PathVariable Long canteenId, 
                        @ModelAttribute Shop shop,
                        @RequestParam("pictureFile") MultipartFile pictureFile,
                        RedirectAttributes redirectAttributes){

            Optional<Canteen> canteenOpt = canteenRepo.findById(canteenId);
            if (canteenOpt.isPresent()) {
                if (!pictureFile.isEmpty()) {
                    try {
                        // Resolve the static folder path at runtime
                        String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath();
                        File uploadDirFile = new File(uploadDir);

                        // Ensure the directory exists
                        if (!uploadDirFile.exists()) {
                            uploadDirFile.mkdirs();  // Create the directory if it doesn't exist
                        }

                        String fileName = pictureFile.getOriginalFilename();
                        File uploadFile = new File(uploadDirFile, fileName);  // Create the full path for the file
                        pictureFile.transferTo(uploadFile);  // Transfer the uploaded file to the specified directory
                        
                        // Log the absolute path for debugging
                        // System.out.println(uploadFile.getAbsolutePath());
                        shop.setPicture("/uploads/" + fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                            redirectAttributes.addFlashAttribute("errorMessages", "File upload failed.");
                            return "redirect:/shop/add/" + canteenId;
                        }
                }
                shop.setCanteen(canteenOpt.get());
                shopRepo.save(shop);

                return "redirect:/canteen/shops/" + canteenId;
            } else {
            return "redirect:/canteens";
        }
    }

    @PostMapping("/shop/delete/{shopId}")
    public String deleteShop(@PathVariable Long shopId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Shop> shopOpt = shopRepo.findById(shopId);
        if (shopOpt.isPresent()) {
            Shop shop = shopOpt.get();
            Long canteenId = shop.getCanteen() != null ? shop.getCanteen().getCanteenId() : null; // Get canteenId if canteen is not null

            shopRepo.delete(shop); // Remove the shop from the database

            // Optionally, delete the shop's picture file if it exists
            String pictureFilePath = "src/main/resources/static/uploads/" + shop.getPicture();
            File pictureFile = new File(pictureFilePath);
            if (pictureFile.exists()) {
                pictureFile.delete();  // Delete the image from the file system
            }

            redirectAttributes.addFlashAttribute("successMessage", "Shop deleted successfully.");
            return "redirect:/canteen/shops/" + canteenId;  // Redirect to the list of shops
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Shop not found.");
            return "redirect:/canteens"; // Redirect to the canteens page if shop not found
        }
    }

    @GetMapping("/shop/edit/{shopId}")
    public String showEditShopForm(@PathVariable Long shopId, Model model) {
        Shop shop = shopService.getShopById(shopId);
        Long canteenId = shop.getCanteen() != null ? shop.getCanteen().getCanteenId() : null;  // Retrieve the canteenId
        model.addAttribute("shop", shop);
        System.out.println(canteenId);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        model.addAttribute("canteenId", canteenId);  // Add canteenId to the model
        return "edit-shop";  // Return the edit-shop template
    }
    

    @PostMapping("/shop/edit/{shopId}")
    public String updateShop(@PathVariable Long shopId,
                            @ModelAttribute Shop shop,
                            @RequestParam(value = "pictureFile", required = false) MultipartFile pictureFile){
                            
        Shop existingShop = shopService.getShopById(shopId);

        if(pictureFile != null && !pictureFile.isEmpty()){
            String newPictureFileName = fileStorageService.storeFile(pictureFile);
            System.out.println(newPictureFileName);
            existingShop.setPicture("/uploads/"+newPictureFileName);
        }else{
            shop.setPicture(existingShop.getPicture());
        }
        shopService.updateShop(shopId, shop);
        
        Long canteenId = existingShop.getCanteen() != null ? existingShop.getCanteen().getCanteenId() : null;
        return "redirect:/canteen/shops/" + canteenId;
    }


}
