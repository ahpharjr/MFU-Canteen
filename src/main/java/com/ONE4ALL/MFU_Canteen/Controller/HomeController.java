package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.CanteenRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;

@Controller
@RequestMapping("/user")
public class HomeController {

    @Autowired
    private CanteenRepository canteenRepo;

    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private ItemRepository itemRepo;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("canteens", canteenRepo.findAll()); // Fetch all canteens
        model.addAttribute("selectedCanteenName", "");

        return "home";
    }

    @GetMapping("/canteen/shops/{canteenId}")
    @ResponseBody
    public List<Shop> getShopsByCanteenId(@PathVariable Long canteenId) {
        return shopRepo.findByCanteen_CanteenId(canteenId);
    }

    @GetMapping("/canteen/shops/{canteenId}/items")
    @ResponseBody
    public List<Item> getItemsByCanteenId(@PathVariable Long canteenId) {
        return itemRepo.findItemsByCanteenId(canteenId);
    }

}

