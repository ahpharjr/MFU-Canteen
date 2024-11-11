package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.CanteenRepository;
import com.ONE4ALL.MFU_Canteen.Repository.CartRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ItemRepository;
import com.ONE4ALL.MFU_Canteen.Repository.ShopRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

@Controller
@RequestMapping("/user")
public class HomeController {

    @Autowired
    private CanteenRepository canteenRepo;

    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

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
    public List<Item> getItemsByCanteenId(@PathVariable Long canteenId) {
        return itemRepo.findItemsByCanteenId(canteenId);
    }

}

