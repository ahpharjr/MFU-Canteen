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
        System.out.println("HomeController.showHomePage()---------------------------------1----");
        model.addAttribute("canteens", canteenRepo.findAll()); // Fetch all canteens
        model.addAttribute("selectedCanteenName", "");
        System.out.println("HomeController.showHomePage()---------------------------------2----");
        return "home";
    }

    @GetMapping("/canteen/shops/{canteenId}")
    @ResponseBody
    public List<Shop> getShopsByCanteenId(@PathVariable Long canteenId) {
        return shopRepo.findByCanteen_CanteenId(canteenId);
    }

    // @GetMapping("/canteen/items/{canteenId}")
    // @ResponseBody
    // public List<Item> getItemsByCanteenId(@PathVariable Long canteenId) {
    //     List<Shop> shops = shopRepo.findByCanteen_CanteenId(canteenId);
    //     List<Item> items = new ArrayList<>();
    //     for (Shop shop : shops) {
    //         items.addAll(itemRepo.findByShop_ShopId(shop.getShopId()));
    //     }
    //     return items;
    // }

    @GetMapping("/shops/{canteenId}/items")
    @ResponseBody
    public List<Item> getItemsByCanteenId(@PathVariable Long canteenId) {
        return itemRepo.findItemsByCanteenId(canteenId);
    }

}

