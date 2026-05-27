package com.moneywise.backend.dto;

import com.moneywise.backend.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileResponseDTO(
        UUID id,
        String name,
        String email,
        LocalDateTime createdAt
) {
    public static ProfileResponseDTO from(User user) {
        return new ProfileResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}