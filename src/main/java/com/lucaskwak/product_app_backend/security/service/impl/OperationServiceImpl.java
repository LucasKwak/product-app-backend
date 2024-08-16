package com.lucaskwak.product_app_backend.security.service.impl;

import com.lucaskwak.product_app_backend.security.persistence.entity.Operation;
import com.lucaskwak.product_app_backend.security.persistence.repository.OperationRepository;
import com.lucaskwak.product_app_backend.security.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public List<Operation> findAllPublicOperations() {
        return operationRepository.findByPermitAllIsTrue();
    }
}
