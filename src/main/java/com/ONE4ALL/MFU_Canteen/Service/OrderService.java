package com.ONE4ALL.MFU_Canteen.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Cart;
import com.ONE4ALL.MFU_Canteen.Entity.CartItem;
import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.OrderItem;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Entity.User;
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

    public List<Order> createOrdersFromSelectedCartItems(Cart cart, List<CartItem> selectedCartItems) {
        if (selectedCartItems.isEmpty()) {
            throw new IllegalArgumentException("No items selected for the order");
        }

        // Group cart items by shop
        Map<Shop, List<CartItem>> itemsGroupedByShop = selectedCartItems.stream()
            .collect(Collectors.groupingBy(cartItem -> cartItem.getItem().getShop()));

        List<Order> orders = new ArrayList<>();

        // Create one order per shop
        for (Map.Entry<Shop, List<CartItem>> entry : itemsGroupedByShop.entrySet()) {
            Shop shop = entry.getKey();
            List<CartItem> shopItems = entry.getValue();

            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString().replace("-", "").substring(0, 16));  // Generate 16-char ID
            order.setUser(cart.getUser());
            order.setShop(shop);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("Preparing");

            double totalPrice = 0.0;

            for (CartItem cartItem : shopItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setItem(cartItem.getItem());
                orderItem.setQuantity(cartItem.getQuantity());
                totalPrice += cartItem.getItem().getPrice() * cartItem.getQuantity();

                order.getOrderItems().add(orderItem);
            }

            order.setTotalPrice(totalPrice);
            orders.add(orderRepository.save(order)); // Save each order to the database
        }

        return orders;
    }


    public Order createOrder(User user, Shop shop, List<OrderItem> orderItems){
        Order order = new Order();
        order.setUser(user);
        order.setShop(shop);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Preparing");

        double totalPrice = orderItems.stream()
                            .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                            .sum();

        order.setTotalPrice(totalPrice);

        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }
    
}
