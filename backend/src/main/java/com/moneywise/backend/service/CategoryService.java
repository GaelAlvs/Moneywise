package com.moneywise.backend.service;

import com.moneywise.backend.dto.CategoryRequestDTO;
import com.moneywise.backend.dto.CategoryResponseDTO;
import com.moneywise.backend.entity.Category;
import com.moneywise.backend.entity.Category.CategoryType;
import com.moneywise.backend.entity.User;
import com.moneywise.backend.exception.BusinessException;
import com.moneywise.backend.repository.CategoryRepository;
import com.moneywise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponseDTO create(CategoryRequestDTO dto, String email) {
        User user = findUser(email);

        Category category = Category.builder()
                .name(dto.name())
                .type(dto.type())
                .icon(dto.icon())
                .color(dto.color())
                .user(user)
                .build();

        return CategoryResponseDTO.from(categoryRepository.save(category));
    }

    public List<CategoryResponseDTO> findAll(String email) {
        User user = findUser(email);
        return categoryRepository.findByUserId(user.getId())
                .stream()
                .map(CategoryResponseDTO::from)
                .toList();
    }

    public List<CategoryResponseDTO> findByType(String email, CategoryType type) {
        User user = findUser(email);
        return categoryRepository.findByUserIdAndType(user.getId(), type)
                .stream()
                .map(CategoryResponseDTO::from)
                .toList();
    }

    public CategoryResponseDTO update(UUID id, CategoryRequestDTO dto, String email) {
        User user = findUser(email);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Acesso negado");
        }

        category.setName(dto.name());
        category.setType(dto.type());
        category.setIcon(dto.icon());
        category.setColor(dto.color());

        return CategoryResponseDTO.from(categoryRepository.save(category));
    }

    public void delete(UUID id, String email) {
        User user = findUser(email);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Acesso negado");
        }

        categoryRepository.delete(category);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }
}