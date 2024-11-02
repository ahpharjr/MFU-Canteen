package com.ONE4ALL.MFU_Canteen.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// public class WebSecurityConfig {

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }




// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter {

//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http
//             .authorizeRequests()
//             .antMatchers("/admin/**").hasRole("ADMIN")
//             .antMatchers("/shop-owner/**").hasRole("OWNER")
//             .antMatchers("/customer/**").hasRole("CUSTOMER")
//             .antMatchers("/public/**").permitAll()
//             .anyRequest().authenticated()
//             .and()
//             .formLogin()
//             .loginPage("/login")
//             .permitAll()
//             .and()
//             .logout()
//             .permitAll();
//     }

//     @Autowired
//     public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//         auth.inMemoryAuthentication()
//             .withUser("admin").password("{noop}admin123").roles("ADMIN")
//             .and()
//             .withUser("owner").password("{noop}owner123").roles("OWNER")
//             .and()
//             .withUser("customer").password("{noop}customer123").roles("CUSTOMER");
//     }
// }

