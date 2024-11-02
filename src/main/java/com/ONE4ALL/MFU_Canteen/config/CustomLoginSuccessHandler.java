package com.ONE4ALL.MFU_Canteen.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

import com.ONE4ALL.MFU_Canteen.Entity.User;
import com.ONE4ALL.MFU_Canteen.Repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // Retrieve the logged-in user’s details
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Redirect to the user-specific home page
            response.sendRedirect("/user/" + user.getId() + "/home");
        } else {
            // Redirect to a fallback page if user not found (shouldn’t happen in normal use)
            response.sendRedirect("/login?error");
        }
    }
}
