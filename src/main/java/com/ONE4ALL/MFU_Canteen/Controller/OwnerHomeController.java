package com.ONE4ALL.MFU_Canteen.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;
import com.ONE4ALL.MFU_Canteen.Service.FileUploadService;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;
import com.ONE4ALL.MFU_Canteen.Service.OwnerService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;

@Controller
@RequestMapping("/owner")
public class OwnerHomeController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/{ownerId}/home")
    public String showOwnerDashboard(@PathVariable Long ownerId, Model model) {
        Owner owner = ownerService.getOwnerById(ownerId);
        List<Shop> shops = shopService.getShopsByOwner(ownerId);
        model.addAttribute("shops", shops);
        model.addAttribute("owner", owner);
        model.addAttribute("ownerId", ownerId);
        return "owner-menu";
    }

    @GetMapping("/shop/{shopId}/create-item")
    public String showCreateItemForm(@PathVariable Long shopId, Model model, 
                                     @RequestParam Long ownerId) {
        model.addAttribute("item", new Item());
        model.addAttribute("shopId", shopId);
        model.addAttribute("ownerId", ownerId);
        return "create-item";
    }

    @PostMapping("/shop/{shopId}/create-item")
    @ResponseBody
    public Item createItem(@PathVariable Long shopId, 
                        @ModelAttribute Item item, 
                        @RequestParam Long ownerId,
                        @RequestParam("image") MultipartFile imageFile) {
        try {
            // Upload the image and set its URL on the item
            String imageUrl = fileUploadService.uploadFile(imageFile);
            item.setImageUrl(imageUrl);

            // Set the default availability
            item.setAvailability(true);

            // Save the item to the shop
            itemService.saveItem(item, shopId);

            // Return the item with the image URL and other data set, as JSON
            return item;

        } catch (IOException e) {
            e.printStackTrace();
            // Handle error appropriately
            return null;
        }
    }

    @GetMapping("/shop/{shopId}/items")
    @ResponseBody
    public List<Item> getItemsByShop(@PathVariable Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        return shop.getItems();
    }
}

