package com.ONE4ALL.MFU_Canteen.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.CartItem;
import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.OrderItem;
import com.ONE4ALL.MFU_Canteen.Repository.OrderRepository;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrderFromCart(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Preparing");

        double totalPrice = 0.0;

        //Copy each cart item into an order item
        for(CartItem cartItem: cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(cartItem.getItem());
            orderItem.setQuantity(cartItem.getQuantity());
            totalPrice += cartItem.getItem().getPrice() * cartItem.getQuantity();

            order.getOrderItems().add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        
        return orderRepository.save(order);
    }

    public Order createOrderFromSelectedCartItems(Cart cart, List<CartItem> selectedCartItems) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString().replace("-", "").substring(0, 16));  // Generate 16-char ID
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Preparing");
    
        double totalPrice = 0.0;
    
        for (CartItem cartItem : selectedCartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(cartItem.getItem());
            orderItem.setQuantity(cartItem.getQuantity());
            totalPrice += cartItem.getItem().getPrice() * cartItem.getQuantity();
    
            order.getOrderItems().add(orderItem);
        }
    
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }
    
}
