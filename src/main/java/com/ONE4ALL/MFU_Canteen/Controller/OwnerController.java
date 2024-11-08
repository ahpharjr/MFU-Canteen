package com.ONE4ALL.MFU_Canteen.Controller;

import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.Role;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.RoleRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;
import com.ONE4ALL.MFU_Canteen.Service.OwnerService;

@Controller
@RequestMapping("/ad")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/owner/add")
    public String showOwnerForm(Model model){
        model.addAttribute("owner", new Owner());

        return "add-owner";
    }

    // @PostMapping("/owner/add")
    // public String addOwner(@ModelAttribute Owner owner){
    //     ownerService.createOwner(owner);

    //     return "redirect:/ad/canteens";
    // }

    @PostMapping("/owner/add")
    public String addOwner(@ModelAttribute Owner owner) {
        // Create and assign the "ROLE_OWNER" role
        Role ownerRole = roleRepository.findByName("ROLE_OWNER");
        if (ownerRole == null) {
            ownerRole = new Role();
            ownerRole.setName("ROLE_OWNER");
            roleRepository.save(ownerRole);
        }

        // Create a new user for the owner
        User user = new User();
        user.setUsername(owner.getEmail()); // You can use email as the username
        user.setPassword(passwordEncoder.encode(owner.getPassword()));  // Encrypt password
        user.getRoles().add(ownerRole);

        // Save the new user
        userRepository.save(user);

        // Link the new user to the owner
        owner.setUser(user);  // Ensure the owner is associated with this user
        ownerService.createOwner(owner);

        return "redirect:/ad/canteens";  // Redirect admin after creating owner
    }


    @PostMapping("/owner/delete/{ownerId}")
    public String deleteOwner(@PathVariable Long ownerId){
        ownerService.deleteOwner(ownerId);

        return "redirect:/ad/canteens";

    }

    @GetMapping("/owner/edit/{ownerId}")
    public String showEditOwnerForm(@PathVariable Long ownerId, Model model){
        Owner owner = ownerService.getOwnerById(ownerId);
        model.addAttribute("owner", owner);

        return "edit-owner";

    }

    @PostMapping("/owner/edit/{ownerId}")
    public String updateOwner(@PathVariable Long ownerId,
                              @ModelAttribute Owner owner){
        
        owner.setOwnerId(ownerId);
        ownerService.updateOwner(owner);

        return "redirect:/ad/canteens";

    }


}
