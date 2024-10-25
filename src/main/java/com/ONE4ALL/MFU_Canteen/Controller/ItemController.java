package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;


@Controller
@RequestMapping("/owner")
public class ItemController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ItemService itemService;

    // Method to show shops owned by a specific owner
    @GetMapping("/shops/{ownerId}")
    public String showOwnerShops(@PathVariable Long ownerId, Model model) {
        System.out.println("-----------------------------ItemController.showOwnerShops()");
        List<Shop> shops = shopService.getShopsByOwner(ownerId); // Get shops for the specific owner
        System.out.println("-----------------------------ItemController.showOwnerShops()");
        model.addAttribute("shops", shops);
        model.addAttribute("ownerId", ownerId); // Pass the ownerId for later use
        return "owner-shops"; // Template to display the list of shops
    }

    // Method to show items in a specific shop
    @GetMapping("/shop/{shopId}/items")
    public String showShopItems(@PathVariable Long shopId, Model model) {
        Shop shop = shopService.getShopById(shopId); // Get the specific shop
        List<Item> items = itemService.getItemsByShop(shopId); // Get items for the shop
        model.addAttribute("shop", shop);
        model.addAttribute("items", items);
        return "shop-items"; // Template to display the items in the shop
    }
}


// @Controller
// @RequestMapping("/owner")
// public class ItemController {

//     @Autowired
//     private ItemService itemService;
    

//     // Show all items
//     @GetMapping("/items")
//     public String showItems(Model model){
//         model.addAttribute("items", itemService.getAllItems());
//         return "item-list";
//     }

//     // Show items for a specific shop
//     @GetMapping("/shop/{shopId}/items")
//     public String showItemsByShop(@PathVariable Long shopId, Model model) {
//         List<Item> items = itemService.getItemsByShopId(shopId); // Method to fetch items by shopId
//         model.addAttribute("items", items);
//         model.addAttribute("shopId", shopId); // Add shopId to the model for reference
//         return "item-list"; // Use the same item-list.html for displaying
//     }
// }
