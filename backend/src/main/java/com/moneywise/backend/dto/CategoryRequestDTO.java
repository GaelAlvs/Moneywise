package com.moneywise.backend.dto;

import com.moneywise.backend.entity.Category.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotNull(message = "Tipo é obrigatório")
    CategoryType type,

    String icon,
    String color
) {}