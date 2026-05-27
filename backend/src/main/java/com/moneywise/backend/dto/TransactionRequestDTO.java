package com.moneywise.backend.dto;

import com.moneywise.backend.entity.Transaction.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequestDTO(

    @NotBlank(message = "Descrição é obrigatória")
    String description,

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    BigDecimal amount,

    @NotNull(message = "Data é obrigatória")
    LocalDate date,

    @NotNull(message = "Tipo é obrigatório")
    TransactionType type,

    String notes,

    @NotNull(message = "Categoria é obrigatória")
    UUID categoryId
) {}