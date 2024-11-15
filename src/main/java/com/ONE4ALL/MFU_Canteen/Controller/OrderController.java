package com.ONE4ALL.MFU_Canteen.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.OrderRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

@Controller
@RequestMapping("/user/{userId}")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    @GetMapping("/orders")
    public String showOrders(@PathVariable Long userId, Model model){

        model.addAttribute("userId", userId);
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            List <Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

        // Format order dates and times
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            orders.forEach(order -> {
                order.setFormattedOrderDate(order.getOrderDate().format(dateFormatter));
                order.setFormattedOrderTime(order.getOrderDate().format(timeFormatter));
            });
            model.addAttribute("orders", orders);

            Cart cart = user.getCart();
            model.addAttribute("totalCartQuantity", cart.getTotalQuantity());
        }
        return "order";
    }
}
