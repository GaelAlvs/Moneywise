package com.moneywise.backend.dto;

import com.moneywise.backend.entity.Category;
import com.moneywise.backend.entity.Category.CategoryType;

import java.util.UUID;

public record CategoryResponseDTO(
    UUID id,
    String name,
    CategoryType type,
    String icon,
    String color
) {
    public static CategoryResponseDTO from(Category category) {
        return new CategoryResponseDTO(
            category.getId(),
            category.getName(),
            category.getType(),
            category.getIcon(),
            category.getColor()
        );
    }
}