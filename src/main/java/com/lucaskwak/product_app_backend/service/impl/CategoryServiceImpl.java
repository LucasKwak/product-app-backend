package com.lucaskwak.product_app_backend.service.impl;

import com.lucaskwak.product_app_backend.dto.inAndOut.CategoryDto;
import com.lucaskwak.product_app_backend.exception.CategoryNotFoundException;
import com.lucaskwak.product_app_backend.persistence.entity.Category;
import com.lucaskwak.product_app_backend.persistence.repository.CategoryRepository;
import com.lucaskwak.product_app_backend.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> findOneById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    @Transactional
    public Category create(CategoryDto saveCategory) {
        Category category = new Category();
        category.setName(saveCategory.getName());
        category.setStatus(Category.CategoryStatus.ENABLED);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long categoryId, CategoryDto saveCategory) {
        Category categoryFromDb = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new CategoryNotFoundException("No se ha encontrado la categoria con id: " + categoryId)
                );

        categoryFromDb.setName(saveCategory.getName());

        return categoryRepository.save(categoryFromDb);
    }

    @Override
    @Transactional
    public Category disable(Long categoryId) {
        Category categoryFromDb = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new CategoryNotFoundException("No se ha encontrado la categoria con id: " + categoryId)
                );

        categoryFromDb.setStatus(Category.CategoryStatus.DISABLED);

        return categoryRepository.save(categoryFromDb);
    }
}
