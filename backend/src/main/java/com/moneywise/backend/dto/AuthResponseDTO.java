package com.moneywise.backend.dto;

public record AuthResponseDTO(
    String token,
    String name,
    String email
) {}