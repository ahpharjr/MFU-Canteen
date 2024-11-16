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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.CartItem;
import com.ONE4ALL.MFU_Canteen.Entity.Item;
import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.CartItemRepository;
import com.ONE4ALL.MFU_Canteen.Repository.CartRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;
import com.ONE4ALL.MFU_Canteen.Service.ItemService;
import com.ONE4ALL.MFU_Canteen.Service.OrderService;

@Controller
@RequestMapping("/user/{userId}")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/cart")
    public String showCart(@PathVariable Long userId, Model model) {
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
            model.addAttribute("totalQuantity", cart.getTotalQuantity());
            model.addAttribute("totalPrice", cart.getTotalPrice());
            model.addAttribute("isCartEmpty", cart.getCartItems().isEmpty());
    
            if (!cart.getCartItems().isEmpty()) {
                CartItem firstCartItem = cart.getCartItems().get(0);
                model.addAttribute("firstCartItem", firstCartItem);
            }
        } else {
            model.addAttribute("isCartEmpty", true);
        }
    
        return "cart";
    }
    
    @PostMapping("/cart")
    public String addItemToCart(@PathVariable Long userId, 
                                @RequestParam Long itemId, Model model) {
        
        model.addAttribute("itemId", itemId);
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
            model.addAttribute("item", item);
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

    @PostMapping("/cart/{cartItemId}/add")
    public String increaseCartItemQuantity(@PathVariable Long userId, @PathVariable Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
        }
        return "redirect:/user/" + userId + "/cart";
    }

    @PostMapping("/cart/{cartItemId}/deduct")
    public String decreaseCartItemQuantity(@PathVariable Long userId, @PathVariable Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null && cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else if (cartItem != null && cartItem.getQuantity() == 1) {
            cartItemRepository.delete(cartItem); // Remove item if quantity reaches zero
        }
        return "redirect:/user/" + userId + "/cart";
    }

    @PostMapping("/cart/{cartItemId}/delete")
    public String deleteCartItem(@PathVariable Long userId, @PathVariable Long cartItemId, RedirectAttributes redirectAttributes) {
        // Call the service to delete the cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        cartItemRepository.delete(cartItem);
        redirectAttributes.addFlashAttribute("message", "Item removed from cart.");
        return "redirect:/user/" + userId + "/cart"; // Redirect back to cart
    }

    @PostMapping("/cart/checkout")
    public String checkout(@PathVariable Long userId, 
                        @RequestParam("selectedItemIds") List<Long> selectedItemIds,
                        RedirectAttributes redirectAttributes) {

        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getCart() != null) {
            Cart cart = user.getCart();

            // Filter cart items to include only selected items
            List<CartItem> selectedCartItems = cart.getCartItems().stream()
                .filter(cartItem -> selectedItemIds.contains(cartItem.getId()))
                .collect(Collectors.toList());

            // Create orders for selected items
            List<Order> orders = orderService.createOrdersFromSelectedCartItems(cart, selectedCartItems);

            // Remove selected items from the cart
            selectedCartItems.forEach(cartItem -> cart.getCartItems().remove(cartItem));
            cartRepository.save(cart);

            // Add a success message for the user
            redirectAttributes.addFlashAttribute("message", "Orders placed successfully. Order IDs: " +
                    orders.stream().map(Order::getOrderId).collect(Collectors.joining(", ")));
        }

        return "redirect:/user/" + userId + "/orders";
    }

    @PostMapping("/cart/ajax-add")
    @ResponseBody
    public Map<String, Integer> addItemToCartAjax(@PathVariable Long userId, @RequestParam Long itemId) {
        User user = userRepository.findById(userId).orElse(null);
        int totalQuantity = 0;
    
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
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                } else {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setItem(item);
                    newCartItem.setQuantity(1);
                    cart.getCartItems().add(newCartItem);
                }
                cartRepository.save(cart);
    
                // Calculate the total quantity
                totalQuantity = cart.getCartItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
            }
        }
    
        // Return updated total quantity
        Map<String, Integer> response = new HashMap<>();
        response.put("totalQuantity", totalQuantity);
        return response;
    }
    
    @GetMapping("/cart/total-quantity")
    @ResponseBody
    public Map<String, Integer> getTotalCartQuantity(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        int totalQuantity = 0;

        if (user != null && user.getCart() != null) {
            totalQuantity = user.getCart().getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("totalQuantity", totalQuantity);
        return response;
    }

}

