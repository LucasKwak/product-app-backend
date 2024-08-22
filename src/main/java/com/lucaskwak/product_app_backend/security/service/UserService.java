package com.lucaskwak.product_app_backend.security.service;


import com.lucaskwak.product_app_backend.security.dto.in.UserDto;
import com.lucaskwak.product_app_backend.security.persistence.entity.User;

import java.util.Optional;

public interface UserService {

    public User registerOneCustomer(UserDto saveUser);

    public Optional<User> findOneUserByUsername(String username);

    public Optional<User> findOneUserByEmail(String email);
}
