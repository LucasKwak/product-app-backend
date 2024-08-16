package com.lucaskwak.product_app_backend.security.service.impl;

import com.lucaskwak.product_app_backend.security.dto.in.UserDto;
import com.lucaskwak.product_app_backend.security.exception.DefaultRoleNotFoundException;
import com.lucaskwak.product_app_backend.security.persistence.entity.Role;
import com.lucaskwak.product_app_backend.security.persistence.entity.User;
import com.lucaskwak.product_app_backend.security.persistence.repository.UserRepository;
import com.lucaskwak.product_app_backend.security.service.RoleService;
import com.lucaskwak.product_app_backend.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User registerOneCustomer(UserDto saveUser) {

        User user = new User();
        user.setName(saveUser.getName());
        user.setUsername(saveUser.getUsername());
        user.setPassword(passwordEncoder.encode(saveUser.getPassword()));

        Role defaultRole = roleService.findDefaultRole().orElseThrow(() -> new DefaultRoleNotFoundException("No se ha encontrado un rol por defecto"));

        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
