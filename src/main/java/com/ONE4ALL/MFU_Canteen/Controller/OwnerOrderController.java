package com.ONE4ALL.MFU_Canteen.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ONE4ALL.MFU_Canteen.Entity.Order;
import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Shop;
import com.ONE4ALL.MFU_Canteen.Service.OwnerOrderService;
import com.ONE4ALL.MFU_Canteen.Service.OwnerService;
import com.ONE4ALL.MFU_Canteen.Service.ShopService;

@Controller
@RequestMapping("/owner")
public class OwnerOrderController {

    @Autowired
    private OwnerService ownerService;

    @Autowired 
    private ShopService shopService;

    @Autowired
    private OwnerOrderService ownerOrderService;
    
    @GetMapping("/{ownerId}/orders")
    public String showOrders(@PathVariable Long ownerId, Model model,
                            @RequestParam Long shopId) {

        Shop shop = shopService.getShopById(shopId);
        Owner owner = ownerService.getOwnerById(ownerId);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("owner", owner);
        model.addAttribute("shop", shop);
        model.addAttribute("shopId", shopId);

        List<Order> orders = ownerOrderService.getOrdersForOwner(ownerId);

        //filter orders to only includ those that are "Preparing"
        orders = orders.stream()
                        .filter(order -> "Preparing".equals(order.getStatus()))
                        .collect(Collectors.toList());

        orders = ownerOrderService.formatOrdersForDisplay(orders); // Apply formatting here
        model.addAttribute("orders", orders);

        return "owner-order-page";
    }

    @PostMapping("/{ownerId}/complete-order/{orderId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeOrder(@PathVariable Long ownerId, @PathVariable String orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Mark the order as completed
            ownerOrderService.markOrderAsCompleted(orderId);

            // Get the updated count of remaining orders
            List<Order> remainingOrders = ownerOrderService.getOrdersForOwner(ownerId)
                                                        .stream()
                                                        .filter(order -> "Preparing".equals(order.getStatus()))
                                                        .collect(Collectors.toList());

            response.put("success", true);
            response.put("remainingOrders", remainingOrders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @GetMapping("/{ownerId}/order-history")
    public String showCompletedOrders(@PathVariable Long ownerId, Model model, @RequestParam Long shopId) {
        Shop shop = shopService.getShopById(shopId);
        Owner owner = ownerService.getOwnerById(ownerId);
    
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("owner", owner);
        model.addAttribute("shop", shop);
        model.addAttribute("shopId", shopId);
    
        List<Order> getOrders = ownerOrderService.getPreparedOrdersForOwner(ownerId);
        List<Order> completedOrders = ownerOrderService.getCompletedOrdersForOwner(ownerId);
        completedOrders = ownerOrderService.formatOrdersForDisplay(completedOrders);
        model.addAttribute("getOrders", getOrders);
        model.addAttribute("completedOrders", completedOrders);
    
        return "completed-orders-history";
    }    
    

}
