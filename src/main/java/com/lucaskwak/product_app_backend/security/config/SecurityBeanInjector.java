package com.lucaskwak.product_app_backend.security.config;

import com.lucaskwak.product_app_backend.security.persistence.entity.User;
import com.lucaskwak.product_app_backend.security.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class SecurityBeanInjector {

    private final UserRepository userRepository;

    @Autowired
    public SecurityBeanInjector(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    // Como es @Bean, no hace falta poner el @Autowired del authenticationConfiguration
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            // Este metodo es usado por el authenticate del AuthenticationManager
            // Se llama a este metodo cuando intentamos hacer un login
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> userDetails = userRepository.findByUsername(username);
                if (userDetails.isPresent()) {
                    // Podemos devolver nuestra entidad User porque User implementa a UserDetails
                    return userDetails.get();
                }else {
                    throw new UsernameNotFoundException(username);
                }
            }
        };
    }
}
