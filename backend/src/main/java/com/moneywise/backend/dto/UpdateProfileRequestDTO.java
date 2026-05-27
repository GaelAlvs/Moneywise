package com.moneywise.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String newPassword,

        String currentPassword
) {}