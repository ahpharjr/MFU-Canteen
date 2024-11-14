package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/{userId}")
public class OrderController {
    
    @GetMapping("/orders")
    public String showOrders(){
        return "order";
    }
}
