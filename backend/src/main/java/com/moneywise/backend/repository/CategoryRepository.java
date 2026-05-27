package com.moneywise.backend.repository;

import com.moneywise.backend.entity.Category;
import com.moneywise.backend.entity.Category.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserId(UUID userId);
    List<Category> findByUserIdAndType(UUID userId, CategoryType type);
}