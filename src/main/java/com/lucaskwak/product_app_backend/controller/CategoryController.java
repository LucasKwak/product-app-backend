package com.lucaskwak.product_app_backend.controller;

import com.lucaskwak.product_app_backend.dto.in.CategoryDto;
import com.lucaskwak.product_app_backend.persistence.entity.Category;
import com.lucaskwak.product_app_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<Category>> findAll(Pageable pageable) {
        Page<Category> categoriesPage = categoryService.findAll(pageable);

        if(categoriesPage.hasContent()) {
            return ResponseEntity.ok(categoriesPage);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> findOneById(@PathVariable Long categoryId) {

        Optional<Category> category = categoryService.findOneById(categoryId);

        if(category.isPresent()) {
            return ResponseEntity.ok(category.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryDto saveCategory) {

        Category category = categoryService.create(saveCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> update(@PathVariable Long categoryId,
                                          @RequestBody CategoryDto saveCategory) {

        Category category = categoryService.update(categoryId, saveCategory);

        return ResponseEntity.ok(category);
    }

    @PatchMapping("/{categoryId}/disabled")
    public ResponseEntity<Category> disable(@PathVariable Long categoryId) {

        Category category = categoryService.disable(categoryId);

        return ResponseEntity.ok(category);
    }
}
