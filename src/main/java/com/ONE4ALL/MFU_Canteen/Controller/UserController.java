package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

@Controller
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        
        return "login";
    }
}
