package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.CartRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;

@Controller
@RequestMapping("/user/{userId}")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/cart")
    public String showCart(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // Add user's cart to the model for the view
            model.addAttribute("cart", user.getCart());
        }
        return "cart";
    }

    @PostMapping("/cart")
    public String addItemToCart(@PathVariable Long userId, 
                                @RequestParam Long itemId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("itemId", itemId);

        // Find the user
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // Check if the user already has a cart
            Cart cart = user.getCart();
            if (cart == null) {
                // Create a new cart if none exists
                cart = new Cart();
                cart.setUser(user);
                user.setCart(cart);
                cartRepository.save(cart); // Save the new cart
            }

            // Find the item to be added
            Item item = itemService.getItemById(itemId);
            if (item != null) {
                boolean itemExists = false;
                for (Item existingItem : cart.getItems()) {
                    if (existingItem.getItemId().equals(item.getItemId())) {
                        // Increase the quantity of the existing item
                        existingItem.setQuantity(existingItem.getQuantity() + 1);
                        itemExists = true;
                        break;
                    }
                }

                if (!itemExists) {
                    // Set initial quantity for the new item
                    item.setQuantity(1);
                    cart.getItems().add(item);
                    item.getCarts().add(cart); // Set the cart for the item
                }

                // Update the total quantity of the cart
                int totalQuantity = cart.getItems().stream().mapToInt(Item::getQuantity).sum();
                cart.setQuantity(totalQuantity);

                // Save the updated cart
                cartRepository.save(cart);
            }
        }

        return "redirect:/user/" + userId + "/cart";
    }

}

