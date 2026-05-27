package com.moneywise.backend.service;

import com.moneywise.backend.dto.DashboardSummaryDTO;
import com.moneywise.backend.dto.TransactionRequestDTO;
import com.moneywise.backend.dto.TransactionResponseDTO;
import com.moneywise.backend.entity.Category;
import com.moneywise.backend.entity.Transaction;
import com.moneywise.backend.entity.Transaction.TransactionType;
import com.moneywise.backend.entity.User;
import com.moneywise.backend.exception.BusinessException;
import com.moneywise.backend.repository.CategoryRepository;
import com.moneywise.backend.repository.TransactionRepository;
import com.moneywise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionResponseDTO create(TransactionRequestDTO dto, String email) {
        User user = findUser(email);
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new BusinessException("Categoria não encontrada"));

        Transaction transaction = Transaction.builder()
                .description(dto.description())
                .amount(dto.amount())
                .date(dto.date())
                .type(dto.type())
                .notes(dto.notes())
                .category(category)
                .user(user)
                .build();

        return TransactionResponseDTO.from(transactionRepository.save(transaction));
    }

    public List<TransactionResponseDTO> findAll(String email) {
        User user = findUser(email);
        return transactionRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(TransactionResponseDTO::from)
                .toList();
    }

    public List<TransactionResponseDTO> findByPeriod(
            String email, LocalDate startDate, LocalDate endDate) {
        User user = findUser(email);
        return transactionRepository
                .findByUserIdAndDateBetweenOrderByDateDesc(user.getId(), startDate, endDate)
                .stream()
                .map(TransactionResponseDTO::from)
                .toList();
    }

    public TransactionResponseDTO update(UUID id, TransactionRequestDTO dto, String email) {
        User user = findUser(email);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transação não encontrada"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Acesso negado");
        }

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new BusinessException("Categoria não encontrada"));

        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setDate(dto.date());
        transaction.setType(dto.type());
        transaction.setNotes(dto.notes());
        transaction.setCategory(category);

        return TransactionResponseDTO.from(transactionRepository.save(transaction));
    }

    public void delete(UUID id, String email) {
        User user = findUser(email);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transação não encontrada"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Acesso negado");
        }

        transactionRepository.delete(transaction);
    }

    public DashboardSummaryDTO getSummary(String email, LocalDate startDate, LocalDate endDate) {
        User user = findUser(email);

        BigDecimal totalIncome = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                user.getId(), TransactionType.INCOME, startDate, endDate);

        BigDecimal totalExpense = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                user.getId(), TransactionType.EXPENSE, startDate, endDate);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new DashboardSummaryDTO(totalIncome, totalExpense, balance);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }
}