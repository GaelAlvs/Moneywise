package com.moneywise.backend.repository;

import com.moneywise.backend.entity.Transaction;
import com.moneywise.backend.entity.Transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUserIdOrderByDateDesc(UUID userId);

    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(
        UUID userId, LocalDate startDate, LocalDate endDate
    );

    List<Transaction> findByUserIdAndTypeOrderByDateDesc(
        UUID userId, TransactionType type
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user.id = :userId AND t.type = :type " +
           "AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal sumByUserIdAndTypeAndDateBetween(
        @Param("userId") UUID userId,
        @Param("type") TransactionType type,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}