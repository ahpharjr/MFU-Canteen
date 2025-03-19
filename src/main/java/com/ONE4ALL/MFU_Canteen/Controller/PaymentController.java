package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.CartItem;
import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.CartRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;
import com.ONE4ALL.MFU_Canteen.Service.OrderService;


@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public PaymentController(OrderService orderService, UserRepository userRepository, CartRepository cartRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/success")
    public ResponseEntity<?> handlePaymentSuccess(@RequestParam Long userId, 
                                                @RequestParam("selectedCartItemIds") String selectedCartItemIds) {
        // Convert `cartItemId`s from String to List<Long>
        List<Long> cartItemIds = Arrays.stream(selectedCartItemIds.split(","))
                                    .map(Long::parseLong)
                                    .collect(Collectors.toList());

        System.out.println("Processing payment success for user ID: " + userId);
        System.out.println("Selected Cart Item IDs: " + cartItemIds);

        // Find user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        if (user.getCart() == null) {
            System.out.println("Cart not found for user!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart not found");
        }

        Cart cart = user.getCart();

        // ✅ Filter cart items using `cartItemId`, not `itemId`
        List<CartItem> selectedCartItems = cart.getCartItems().stream()
                .filter(cartItem -> cartItemIds.contains(cartItem.getId()))
                .collect(Collectors.toList());

        if (selectedCartItems.isEmpty()) {
            System.out.println("No valid cart items found for checkout.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No items selected for the order");
        }

        // Create orders for selected cart items
        List<Order> orders = orderService.createOrdersFromSelectedCartItems(cart, selectedCartItems);

        // Remove selected items from the cart
        selectedCartItems.forEach(cartItem -> cart.getCartItems().remove(cartItem));
        cartRepository.save(cart);

        System.out.println("Order successfully created! Redirecting user to orders page.");


        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/user/" + userId + "/orders")
                .build();
    }

}
