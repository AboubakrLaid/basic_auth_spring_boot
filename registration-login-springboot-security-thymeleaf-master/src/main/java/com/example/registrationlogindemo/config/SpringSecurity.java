package com.example.registrationlogindemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static  PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    



@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        // .cors().configurationSource(corsConfigurationSource()).and()
            .csrf(csrf -> csrf.disable()) // Assume this is still valid; adjust if necessary
            .authorizeHttpRequests(authz -> authz
            
                .requestMatchers("/api/register/**", "/api/login/**","/api/token" ,"/api/logout/**").permitAll()
                .requestMatchers("/api/users/").permitAll()//.hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated())
        //     .httpBasic() // Adjusted for hypothetical new method
            .logout(logout -> logout
                .logoutUrl("/api/logout") // Assuming logout is handled by Spring Security
                .logoutSuccessUrl("/api")
                .permitAll());

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    

    
}
