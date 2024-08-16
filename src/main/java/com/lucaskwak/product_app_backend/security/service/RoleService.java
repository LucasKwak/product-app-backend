package com.lucaskwak.product_app_backend.security.service;


import com.lucaskwak.product_app_backend.security.persistence.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();
}
