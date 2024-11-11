package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Service.OwnerService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;

@Controller
@RequestMapping("/owner")
public class OwnerOrderController {

    @Autowired
    private OwnerService ownerService;

    @Autowired 
    private ShopService shopService;
    
    @GetMapping("/{ownerId}/orders")
    public String showOrders(@PathVariable Long ownerId, Model model,
                             @RequestParam Long shopId){

        Shop shop = shopService.getShopById(shopId);
        Owner owner = ownerService.getOwnerById(ownerId);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("owner", owner);
        model.addAttribute("shop", shop);
        
        return "owner-order-page";
    }
}
