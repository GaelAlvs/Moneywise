package com.moneywise.backend.dto;

import java.math.BigDecimal;

public record DashboardSummaryDTO(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal balance
) {}