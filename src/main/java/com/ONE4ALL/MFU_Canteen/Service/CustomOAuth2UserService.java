package com.ONE4ALL.MFU_Canteen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ONE4ALL.MFU_Canteen.Entity.Role;
import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.RoleRepository;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByUsername(name);

        if (user == null) {
            // Create a new user if not found
            user = new User();
            user.setUsername(name);

            // Assign ROLE_CUSTOMER
            Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
            if (customerRole == null) {
                customerRole = new Role();
                customerRole.setName("ROLE_CUSTOMER");
                roleRepository.save(customerRole);
            }

            user.getRoles().add(customerRole);
            userRepository.save(user);
        }

        return new CustomOAuth2User(oAuth2User, user);
    }
}
