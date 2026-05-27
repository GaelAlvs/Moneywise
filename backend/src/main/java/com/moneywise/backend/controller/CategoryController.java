package com.moneywise.backend.controller;

import com.moneywise.backend.dto.CategoryRequestDTO;
import com.moneywise.backend.dto.CategoryResponseDTO;
import com.moneywise.backend.entity.Category.CategoryType;
import com.moneywise.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @Valid @RequestBody CategoryRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(categoryService.create(dto, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) CategoryType type) {
        if (type != null) {
            return ResponseEntity.ok(categoryService.findByType(userDetails.getUsername(), type));
        }
        return ResponseEntity.ok(categoryService.findAll(userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(categoryService.update(id, dto, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        categoryService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}