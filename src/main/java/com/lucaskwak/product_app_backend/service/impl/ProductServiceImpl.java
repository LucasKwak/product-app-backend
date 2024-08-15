package com.lucaskwak.product_app_backend.service.impl;

import com.lucaskwak.product_app_backend.dto.in.ProductDto;
import com.lucaskwak.product_app_backend.exception.CategoryNotFoundException;
import com.lucaskwak.product_app_backend.exception.ProductNotFoundException;
import com.lucaskwak.product_app_backend.persistence.entity.Category;
import com.lucaskwak.product_app_backend.persistence.entity.Product;
import com.lucaskwak.product_app_backend.persistence.repository.CategoryRepository;
import com.lucaskwak.product_app_backend.persistence.repository.ProductRepository;
import com.lucaskwak.product_app_backend.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findOneById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional
    public Product create(ProductDto saveProduct) {

        Product product = new Product();
        product.setName(saveProduct.getName());
        product.setPrice(saveProduct.getPrice());
        product.setStatus(Product.ProductStatus.ENABLED);

        Optional<Category> category = categoryRepository.findById(saveProduct.getCategoryId());
        if (category.isPresent()) {
            product.setCategory(category.get());
        }else {
            throw new CategoryNotFoundException("No se ha encontrado la categoria con id: " + saveProduct.getCategoryId() + ", cree una primero o asegurese de que exista");
        }

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long productId, ProductDto saveProduct) {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ProductNotFoundException("No se ha encontrado el producto con id: " + productId)
                );

        productFromDb.setName(saveProduct.getName());
        productFromDb.setPrice(saveProduct.getPrice());

        Optional<Category> category = categoryRepository.findById(saveProduct.getCategoryId());
        if (category.isPresent()) {
            productFromDb.setCategory(category.get());
        }else {
            throw new CategoryNotFoundException("No se ha encontrado la categoria con id: " + saveProduct.getCategoryId() + ", cree una primero o asegurese de que exista");
        }

        return productRepository.save(productFromDb);
    }

    @Override
    @Transactional
    public Product disable(Long productId) {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ProductNotFoundException("No se ha encontrado el producto con id: " + productId)
                );
        productFromDb.setStatus(Product.ProductStatus.DISABLED);

        return productRepository.save(productFromDb);
    }
}
