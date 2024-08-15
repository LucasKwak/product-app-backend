package com.lucaskwak.product_app_backend.service;

import com.lucaskwak.product_app_backend.dto.inAndOut.CategoryDto;
import com.lucaskwak.product_app_backend.persistence.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findOneById(Long categoryId);

    Category create(CategoryDto saveCategory);

    Category update(Long categoryId, CategoryDto saveCategory);

    Category disable(Long categoryId);
}
