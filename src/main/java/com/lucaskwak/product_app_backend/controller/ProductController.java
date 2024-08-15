package com.lucaskwak.product_app_backend.controller;

import com.lucaskwak.product_app_backend.dto.in.ProductDto;
import com.lucaskwak.product_app_backend.persistence.entity.Product;
import com.lucaskwak.product_app_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> findAll(Pageable pageable) {

        Page<Product> productsPage = productService.findAll(pageable);

        if(productsPage.hasContent()) {
            return ResponseEntity.ok(productsPage);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findOneById(@PathVariable Long productId) {

        Optional<Product> product = productService.findOneById(productId);

        if(product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }

        return ResponseEntity.notFound().build();
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDto saveProduct) {

        Product product = productService.create(saveProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> update(@PathVariable Long productId,
                                          @RequestBody ProductDto saveProduct) {

        Product product = productService.update(productId, saveProduct);

        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{productId}/disabled")
    public ResponseEntity<Product> disable(@PathVariable Long productId) {

        Product product = productService.disable(productId);

        return ResponseEntity.ok(product);
    }
}
