package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.ui.Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Service.OwnerService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;

@Controller
public class OwnerController {
    
    @Autowired
    private ShopService shopService;

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/shop/assignOwner/{shopId}")
    public String showAssignOwnerForm(@PathVariable Long shopId, Model model) {
        Shop shop = shopService.getShopById(shopId);
        List<Owner> owners = ownerService.getAllOwners(); // Get all available owners
        
        model.addAttribute("shop", shop);
        model.addAttribute("owners", owners);
        return "assign-owner"; // Thymeleaf template to assign owner
    }

    @PostMapping("/shop/assignOwner/{shopId}")
    public String assignOwnerToShop(@PathVariable Long shopId, @RequestParam Long ownerId, RedirectAttributes redirectAttributes) {
        Shop shop = shopService.getShopById(shopId);
        Owner owner = ownerService.getOwnerById(ownerId);

        shop.setOwner(owner);  // Set owner for the shop
        shopService.updateShop(shopId, shop);

        redirectAttributes.addFlashAttribute("successMessage", "Owner assigned successfully.");
        return "redirect:/shops";
    }


}
