package com.lucaskwak.product_app_backend.security.service.impl;

import com.lucaskwak.product_app_backend.security.persistence.entity.Role;
import com.lucaskwak.product_app_backend.security.persistence.repository.RoleRepository;
import com.lucaskwak.product_app_backend.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final String defaultRole = "CUSTOMER";

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findDefaultRole() {
        return roleRepository.findByName(defaultRole);
    }
}
