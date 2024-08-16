package com.lucaskwak.product_app_backend.security.service;


import com.lucaskwak.product_app_backend.security.persistence.entity.Operation;

import java.util.List;

public interface OperationService {
    List<Operation> findAllPublicOperations();
}
