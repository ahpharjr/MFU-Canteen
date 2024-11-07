package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
public class MenuController {
    
    @GetMapping("/home/menu")
    public String showMenuPage(){
        return "menu";
    }
}
