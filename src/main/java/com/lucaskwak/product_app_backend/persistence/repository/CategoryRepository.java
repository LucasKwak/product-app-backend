package com.lucaskwak.product_app_backend.persistence.repository;

import com.lucaskwak.product_app_backend.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
