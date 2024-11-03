package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;

@Controller
@RequestMapping("/user/{userId}")
public class ViewItemController {

    @Autowired
    private ItemService itemService;
    
    @GetMapping("/item/{itemId}")
    public String viewItem(@PathVariable Long itemId, @PathVariable Long userId, Model model){
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("sellerName", item.getShop().getOwner().getName());
        model.addAttribute("userId", userId);
        model.addAttribute("itemId", itemId);
        
        return "view-item";
    }
}
