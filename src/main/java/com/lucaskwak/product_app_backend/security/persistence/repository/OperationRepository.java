package com.lucaskwak.product_app_backend.security.persistence.repository;

import com.lucaskwak.product_app_backend.security.persistence.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    // Se podria hacer con una query: @Query("SELECT o FROM Operation o WHERE o.permitAll = true")
    List<Operation> findByPermitAllIsTrue();
}
