package com.lucaskwak.product_app_backend.persistence.repository;

import com.lucaskwak.product_app_backend.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
