package com.ONE4ALL.MFU_Canteen.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.OwnerRepository;
import com.ONE4ALL.MFU_Canteen.Service.FileUploadService;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;


@Controller
@RequestMapping("/owner")
public class ItemController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ItemService itemService;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    FileUploadService fileUploadService;

    // Method to show shops owned by a specific owner
    @GetMapping("/shops/{ownerId}")
    public String showOwnerShops(@PathVariable Long ownerId, Model model) {
        List<Shop> shops = shopService.getShopsByOwner(ownerId); // Get shops for the specific owner
        Optional<Owner> ownerOpt= ownerRepository.findById(ownerId);
        if(ownerOpt.isPresent()){
            Owner owner = ownerOpt.get();
            model.addAttribute("owner", owner);
        }
        
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

    @GetMapping("/item/add/{shopId}")
    public String showAddItemForm(@PathVariable Long shopId, Model model){
        model.addAttribute("item", new Item());
        model.addAttribute("shopId", shopId);
        
        return "add-item";
    }

    @PostMapping("/item/add/{shopId}")
        public String addItem(@PathVariable Long shopId, Item item, @RequestParam("image") MultipartFile imageFile) {

            System.out.println("----------------------------1-----------------------------");
            try {
                System.out.println("----------------------------2-----------------------------");
                // Upload file and set file path
                String imageUrl = fileUploadService.uploadFile(imageFile);
                System.out.println("imageUrl::"+ imageUrl);
                System.out.println("----------------------------3-----------------------------");
                item.setImageUrl(imageUrl);
                System.out.println("----------------------------4-----------------------------");
                // Save the item with image URL
                itemService.saveItem(item, shopId);
                System.out.println("----------------------------5-----------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("----------------------------5-----------------------------");
        return "redirect:/owner/shop/" + shopId + "/items";
    }
}
