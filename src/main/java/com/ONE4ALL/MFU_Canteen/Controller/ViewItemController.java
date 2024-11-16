package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.CartRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;

@Controller
@RequestMapping("/user/{userId}")
public class ViewItemController {

    @Autowired
    private ItemService itemService;

        @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    
    @GetMapping("/item/{itemId}")
    public String viewItem(@PathVariable Long itemId, @PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        
        if (user != null) {
            Cart cart = user.getCart();
            if (cart == null) {
                // Create a new cart for the user if one does not exist
                cart = new Cart();
                cart.setUser(user);
                user.setCart(cart);
                cartRepository.save(cart);
            }
            
            model.addAttribute("cart", cart);
            model.addAttribute("totalQuantity", cart.getTotalQuantity()); // Add totalQuantity to the model
        }
    
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("sellerName", item.getShop().getOwner().getName());
        model.addAttribute("userId", userId);
        model.addAttribute("itemId", itemId);
    
        // Fetch similar items by category
        List<Item> similarItems = itemService.getItemsByCategory(item.getCategory(), itemId);
        model.addAttribute("similarItems", similarItems);
        
        return "view-item";
    }
    
}
