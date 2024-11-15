package com.ONE4ALL.MFU_Canteen.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Repository.OrderRepository;
import com.ONE4ALL.MFU_Canteen.Repository.OwnerRepository;

@Service
public class OwnerOrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OwnerRepository ownerRepository;


    public List<Order> getOrdersForOwner(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner != null) {
            List<Shop> shops = owner.getShops();
            List<Order> orders = orderRepository.findByShopIn(shops);

            // Sort orders by orderDate in descending order
            // orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
            orders.sort(Comparator.comparing(Order::getOrderDate));
            
            return orders;
        }
        return Collections.emptyList();
    }

    public List<Order> formatOrdersForDisplay(List<Order> orders) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a");
        orders.forEach(order -> {
            order.setFormattedOrderDate(order.getOrderDate().format(formatter));
        });
        return orders;
    }

}
