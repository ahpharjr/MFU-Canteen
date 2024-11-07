package com.ONE4ALL.MFU_Canteen.Controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ONE4ALL.MFU_Canteen.Entity.Owner;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;
import com.ONE4ALL.MFU_Canteen.Service.OwnerService;

@Controller
@RequestMapping("/owner")
public class OwnerHomeController {
    
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/{ownerId}/home")
    public String showOwnerDashboard(@PathVariable Long ownerId, Model model) {

        Owner owner = ownerService.getOwnerById(ownerId);  
        if(owner != null){  
            model.addAttribute("owner", owner);
        }

        return "owner-menu";  // This will display the owner's dashboard page
    }
}

