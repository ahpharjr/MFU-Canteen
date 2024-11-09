package com.ONE4ALL.MFU_Canteen.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;

import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        // Check if user has an admin or owner role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = authorities.stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_OWNER"));

        if (isAdmin) {
            response.sendRedirect("/ad/canteens");
        } else if (isOwner) {
            response.sendRedirect("/owner/" + user.getOwner().getOwnerId() +"/home"); // Redirect to owner's dashboard
        } else {
            response.sendRedirect("/user/" + user.getId() + "/home");
        }
    }

}
