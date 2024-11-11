package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner")
public class OwnerOrderController {
    
    @GetMapping("/{ownerId}/orders")
    public String showOrders(@PathVariable Long ownerId, Model model){
        model.addAttribute("ownerId", ownerId);

        return "owner-order-page";
    }
}
