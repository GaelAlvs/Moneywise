package com.moneywise.backend.dto;

import com.moneywise.backend.entity.Transaction;
import com.moneywise.backend.entity.Transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDTO(
    UUID id,
    String description,
    BigDecimal amount,
    LocalDate date,
    TransactionType type,
    String notes,
    CategoryResponseDTO category
) {
    public static TransactionResponseDTO from(Transaction transaction) {
        return new TransactionResponseDTO(
            transaction.getId(),
            transaction.getDescription(),
            transaction.getAmount(),
            transaction.getDate(),
            transaction.getType(),
            transaction.getNotes(),
            CategoryResponseDTO.from(transaction.getCategory())
        );
    }
}