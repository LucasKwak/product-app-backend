package com.lucaskwak.product_app_backend.service;

import com.lucaskwak.product_app_backend.dto.inAndOut.ProductDto;
import com.lucaskwak.product_app_backend.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findOneById(Long productId);

    Product create(ProductDto saveProduct);

    Product update(Long productId, ProductDto saveProduct);

    Product disable(Long productId);
}
