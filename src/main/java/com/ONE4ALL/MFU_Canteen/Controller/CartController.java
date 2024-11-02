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
import com.ONE4ALL.MFU_Canteen.Entity.CartItem;
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
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cartRepository.save(cart);
        }

        Item item = itemService.getItemById(itemId);
        if (item != null) {
            CartItem existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getItemId().equals(itemId))
                .findFirst()
                .orElse(null);

            if (existingCartItem != null) {
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1); // Increment quantity
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(cart);
                newCartItem.setItem(item);
                newCartItem.setQuantity(1); // Initial quantity
                cart.getCartItems().add(newCartItem);
            }
            cartRepository.save(cart);
        }
    }
    return "redirect:/user/" + userId + "/cart";
}


}

