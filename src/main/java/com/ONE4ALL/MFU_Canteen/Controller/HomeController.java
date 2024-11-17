package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.CanteenRepository;
import com.ONE4ALL.MFU_Canteen.Repository.CartRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;
import com.ONE4ALL.MFU_Canteen.Service.FavoriteService;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;

@Controller
@RequestMapping("/user")
public class HomeController {

    @Autowired
    private CanteenRepository canteenRepo;

    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/{id}/home")
    public String showHomePage(@PathVariable Long id,Model model){
        User user = userRepository.findById(id).orElse(null);
        
        if (user != null) {
            Cart cart = user.getCart();
            if (cart == null) {
                // Create a new cart for the user if one does not exist
                cart = new Cart();
                cart.setUser(user);
                user.setCart(cart);
                cartRepository.save(cart);
            }
             
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        }

        model.addAttribute("canteens", canteenRepo.findAll()); 
        model.addAttribute("selectedCanteenName", "");
        model.addAttribute("userId", id);

        return "home";
  
    }
               

    @GetMapping("/canteen/shops/{canteenId}")
    @ResponseBody
    public List<Shop> getShopsByCanteenId(@PathVariable Long canteenId) {
        return shopRepo.findByCanteen_CanteenId(canteenId);
    }

    @GetMapping("/canteen/shops/{canteenId}/items")
    @ResponseBody
    public List<Map<String, Object>> getAvailableItemsByCanteenId(@PathVariable Long canteenId, @RequestParam Long userId) {
        // Ensure this matches exactly
        List<Item> items = itemService.getAvailableItemsByCanteenId(canteenId);
        return items.stream().map(item -> {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("itemId", item.getItemId());
            itemData.put("name", item.getName());
            itemData.put("price", item.getPrice());
            itemData.put("imageUrl", item.getImageUrl());
            itemData.put("category", item.getCategory());
            itemData.put("isFavorite", favoriteService.isItemFavorite(userId, item.getItemId())); // Check favorite status
            return itemData;
        }).collect(Collectors.toList());
    }  

}

