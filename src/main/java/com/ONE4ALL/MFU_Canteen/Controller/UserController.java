package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Role;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.RoleRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        System.out.println("Enter register form--------------------------------------------------1");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // Encrypt the password
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Fetch or create the CUSTOMER role
        System.out.println("Enter register form--------------------------------------------------11");
        Role customerRole = roleRepository.findByName("CUSTOMER");
        if (customerRole == null) {
            customerRole = new Role();
            customerRole.setName("CUSTOMER");
            roleRepository.save(customerRole);
        }
        System.out.println("Enter register form--------------------------------------------------12");
        // Assign CUSTOMER role to the new user
        user.getRoles().add(customerRole);
        userRepository.save(user);
        System.out.println("Enter register form--------------------------------------------------13");
        return "redirect:/user/home";
    }
}
