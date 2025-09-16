package com.api.customer.serviceImpl;

import com.api.customer.dto.RewardSummaryDTO;
import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardServiceImplTest {
    private TransactionRepository repository;
    private RewardServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(TransactionRepository.class);
        service = new RewardServiceImpl(repository);
    }

    @Test
    void getAllTimeSummary_returnsSummary() {
        Transaction t = new Transaction();
        t.setCustomerId("1");
        t.setAmount(120.0);
        t.setTransactionDate(Instant.now());
        when(repository.findByCustomerId("1")).thenReturn(List.of(t));

        RewardSummaryDTO result = service.getAllTimeSummary(1L);
        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        assertTrue(result.getTotalPoints() > 0);
    }

    @Test
    void getAllTimeSummary_noTransactions_throwsNotFound() {
        when(repository.findByCustomerId("1")).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class, () -> service.getAllTimeSummary(1L));
    }

    @Test
    void getMonthlySummary_validMonth_returnsSummary() {
        Transaction t = new Transaction();
        t.setCustomerId("1");
        t.setAmount(90.0);
        t.setTransactionDate(Instant.now());
        when(repository.findByCustomerIdAndTransactionDateBetween(anyString(), any(), any()))
                .thenReturn(List.of(t));

        RewardSummaryDTO result = service.getMonthlySummary(1L, "2025-09");
        assertNotNull(result);
        assertEquals("2025-09", result.getPeriod());
    }
}
