package com.ONE4ALL.MFU_Canteen.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Service.FileUploadService;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;
import com.ONE4ALL.MFU_Canteen.Service.OwnerOrderService;
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

    @Autowired
    private OwnerOrderService ownerOrderService;

    @GetMapping("/{ownerId}/home")
    public String showOwnerDashboard(@PathVariable Long ownerId, Model model) {
        Owner owner = ownerService.getOwnerById(ownerId);
        List<Shop> shops = shopService.getShopsByOwner(ownerId);
        model.addAttribute("shops", shops);
        model.addAttribute("owner", owner);
        model.addAttribute("ownerId", ownerId);

        List<Order> getOrders = ownerOrderService.getPreparedOrdersForOwner(ownerId);
        model.addAttribute("getOrders", getOrders);

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

    // Add in HomeController

    @GetMapping("/shop/{shopId}/edit-item/{itemId}")
    public String showEditItemPage(@PathVariable Long shopId, 
                                   @PathVariable Long itemId, 
                                   @RequestParam Long ownerId,
                                   Model model) {

        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("shopId", shopId);
        model.addAttribute("ownerId", ownerId);
        return "edit-item";
    }

    // Update in OwnerHomeController
    @PostMapping("/shop/{shopId}/edit-item/{itemId}")
    @ResponseBody
    public Item updateItem(@PathVariable Long shopId,
                        @PathVariable Long itemId,
                        @ModelAttribute Item updatedItem,
                        @RequestParam("image") MultipartFile imageFile) {
        Item existingItem = itemService.getItemById(itemId);

        try {
            if (!imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                existingItem.setImageUrl(imageUrl);
            }

            existingItem.setName(updatedItem.getName());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setDescription(updatedItem.getDescription());
            existingItem.setCategory(updatedItem.getCategory());
            existingItem.setAvailability(updatedItem.isAvailability());

            itemService.updateItem(existingItem);
            return existingItem;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // OwnerHomeController.java
    @DeleteMapping("/shop/{shopId}/delete-item/{itemId}")
    @ResponseBody
    public ResponseEntity<String> deleteItem(@PathVariable Long shopId, @PathVariable Long itemId) {
        try {
            itemService.deleteItem(itemId);
            return ResponseEntity.ok("Item deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete item");
        }
    }

    @GetMapping("/{ownerId}/edit-profile")
    public String showEditProfileForm(@PathVariable Long ownerId, Model model){
        System.out.println("OwnerHomeController.showEditProfileForm()::::::::::::::1");
        Owner owner = ownerService.getOwnerById(ownerId);
        System.out.println("owner:::::::::::::::"+ owner);
        model.addAttribute("owner", owner);
        // model.addAttribute("ownerId", ownerId);

        return "update-owner-profile";
    };

    @PostMapping("/{ownerId}/edit-profile")
    @ResponseBody
    public Owner updateProfile(@PathVariable Long ownerId,
                            @ModelAttribute Owner updatedOwner,
                            @RequestParam("image") MultipartFile imageFile) {
        Owner existingOwner = ownerService.getOwnerById(ownerId);

        System.out.println("=====================1===========================");
        try {
            if (!imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                existingOwner.setProfilePicture(imageUrl);
            }

            existingOwner.setName(updatedOwner.getName());
            existingOwner.setEmail(updatedOwner.getEmail());
            existingOwner.setPhNum(updatedOwner.getPhNum());

            System.out.println("=====================12===========================");
            ownerService.updateOwner(existingOwner);
            return existingOwner;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

