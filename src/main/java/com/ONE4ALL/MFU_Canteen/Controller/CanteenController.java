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
import org.springframework.web.bind.annotation.RequestMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Canteen;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.CanteenRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;
import com.ONE4ALL.MFU_Canteen.Service.OwnerService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;

@Controller
@RequestMapping("/ad")
public class CanteenController {
    
    @Autowired
    private CanteenRepository canteenRepo;

    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ShopService shopService;

    @GetMapping("/canteens")
    public String showCanteen(Model model){
        model.addAttribute("canteens", canteenRepo.findAll());
        model.addAttribute("owners", ownerService.getAllOwners());

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

        return "redirect:/ad/canteens"; // Redirect to the canteen list page
    }

    @GetMapping("/canteen/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){

        Optional<Canteen> canteen = canteenRepo.findById(id);
        if (canteen.isPresent()){
            model.addAttribute("canteen", canteen.get());
            return "edit-canteen";
        }else{
            return "redirect:/ad/canteens";
        }
    }

    @PostMapping("/canteen/edit/{id}")
    public String updateCanteen(@PathVariable Long id, @ModelAttribute Canteen canteen){

        canteen.setCanteenId(id);
        canteenRepo.save(canteen);

        return "redirect:/ad/canteens";

    }

    @PostMapping("/canteen/delete/{id}")
    public String deleteCanteen(@PathVariable("id") Long id){
        
        canteenRepo.deleteById(id);
        
        return "redirect:/ad/canteens";
    }

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
            return "redirect:/ad/canteens"; 
        }
    }

    // @GetMapping("/canteen/shops/{canteenId}")
    // public String viewShops(@PathVariable("canteenId") Long canteenId, Model model) {
    //     Optional<Canteen> canteenOpt = canteenRepo.findById(canteenId);
    //     if (canteenOpt.isPresent()) {
    //         Canteen canteen = canteenOpt.get();
    //         List<Shop> shops = shopRepo.findByCanteen(canteen); 
    //         List<Item> recommendedItems = shopService.getRecommendedItemsByCanteen(canteenId); // Fetch recommended items

    //         model.addAttribute("canteen", canteen);
    //         model.addAttribute("shops", shops);
    //         model.addAttribute("recommendedItems", recommendedItems); // Add recommended items to model

    //         return "shop-list"; 
    //     } else {
    //         return "redirect:/ad/canteens"; 
    //     }
    // }
}
