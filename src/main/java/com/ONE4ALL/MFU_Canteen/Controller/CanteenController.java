package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Canteen;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.CanteenRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;

@Controller
public class CanteenController {
    
    @Autowired
    private CanteenRepository canteenRepo;

    @Autowired
    private ShopRepository shopRepo;

    @GetMapping("/canteens")
    public String showCanteen(Model model){
        model.addAttribute("canteens", canteenRepo.findAll());

        return "canteen-list";
    }

    @GetMapping("/canteen/add")
    public String showCanteenForm(Model model){
        model.addAttribute("canteen", new Canteen());
        
        return "add-canteen";
    }

    @PostMapping("/canteen/add")
    public String addCanteen(@ModelAttribute Canteen canteen){

        canteenRepo.save(canteen);

        return "redirect:/canteens"; // Redirect to the canteen list page
    }

    @GetMapping("/canteen/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){

        Optional<Canteen> canteen = canteenRepo.findById(id);
        if (canteen.isPresent()){
            model.addAttribute("canteen", canteen.get());
            return "edit-canteen";
        }else{
            return "redirect:/canteens";
        }
    }

    @PostMapping("/canteen/edit/{id}")
    public String updateCanteen(@PathVariable Long id, @ModelAttribute Canteen canteen){

        canteen.setCanteenId(id);
        canteenRepo.save(canteen);

        return "redirect:/canteens";

    }

    @PostMapping("/canteen/delete/{id}")
    public String deleteCanteen(@PathVariable("id") Long id){
        
        canteenRepo.deleteById(id);
        
        return "redirect:/canteens";
    }

    // @GetMapping("/canteen/shops/{id}")
    // public String viewShops(@PathVariable("id") Long canteenId, Model model) {
    //     List<Shop> shops = shopRepo.findShopsByCanteenId(canteenId);
    //     model.addAttribute("shops", shops);
    //     // model.addAttribute("canteenId", canteenId);

    //     return "shop-list"; 
    // }

    @GetMapping("/canteen/shops/{canteenId}")
    public String viewShops(@PathVariable("canteenId") Long canteenId, Model model) {
        Optional<Canteen> canteenOpt = canteenRepo.findById(canteenId);
        if (canteenOpt.isPresent()) {
            Canteen canteen = canteenOpt.get();
            List<Shop> shops = shopRepo.findByCanteen(canteen); 
            model.addAttribute("canteen", canteen);
            model.addAttribute("shops", shops);
            return "shop-list"; 
        } else {
            return "redirect:/canteens"; 
        }
    }
}
