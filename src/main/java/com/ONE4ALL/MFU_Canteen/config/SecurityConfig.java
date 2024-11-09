package com.ONE4ALL.MFU_Canteen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/register", "/login", "/styles/**", "/images/**", "/icons/**", "/uploads/**").permitAll()
    //             .requestMatchers("/user/**").hasRole("CUSTOMER")    // Customers can access /user/**
    //             .requestMatchers("/ad/**").hasRole("ADMIN")         // Admins can access /ad/**
    //             .requestMatchers("/owner/**").hasRole("OWNER")     // Owners can access /owner/**
    //             .anyRequest().authenticated()                      // All other URLs require authentication
    //         )
    //         .formLogin(form -> form
    //             .loginPage("/login")
    //             .successHandler(customLoginSuccessHandler)     // Custom login handler
    //             .permitAll()
    //         )
    //         .logout(logout -> logout
    //             .logoutUrl("/logout")
    //             .logoutSuccessUrl("/login?logout")
    //             .permitAll()
    //         );

    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login", "/styles/**", "/images/**", "/icons/**", "/uploads/**").permitAll()
                .requestMatchers("/user/**").hasRole("CUSTOMER")
                .requestMatchers("/ad/**").hasRole("ADMIN")
                .requestMatchers("/owner/**").hasRole("OWNER") // Owners can access /owner/**
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // .csrf(csrf -> csrf
            //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Enable CSRF token in cookie, accessible to JavaScript
            // );
         // CSRF token in a cookie, accessible to JS


            .csrf(csrf -> csrf.disable());

            // .csrf(csrf -> csrf
            //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            // ); // CSRF protection with tokens accessible to JavaScript
    
        return http.build();
    }
    
}
