package com.moneywise.backend.service;

import com.moneywise.backend.dto.ProfileResponseDTO;
import com.moneywise.backend.dto.UpdateProfileRequestDTO;
import com.moneywise.backend.entity.User;
import com.moneywise.backend.exception.BusinessException;
import com.moneywise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponseDTO getProfile(String email) {
        User user = findUser(email);
        return ProfileResponseDTO.from(user);
    }

    public ProfileResponseDTO updateProfile(String email, UpdateProfileRequestDTO dto) {
        User user = findUser(email);

        user.setName(dto.name());

        if (dto.newPassword() != null && !dto.newPassword().isBlank()) {
            if (dto.currentPassword() == null || dto.currentPassword().isBlank()) {
                throw new BusinessException("Senha atual é obrigatória para alterar a senha");
            }
            if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
                throw new BusinessException("Senha atual incorreta");
            }
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
        }

        return ProfileResponseDTO.from(userRepository.save(user));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }
}