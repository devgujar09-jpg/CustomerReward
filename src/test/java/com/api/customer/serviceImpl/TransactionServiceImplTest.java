package com.api.customer.serviceImpl;

import com.api.customer.dto.TransactionDTO;
import com.api.customer.exception.InvalidDateRangeException;
import com.api.customer.exception.NotFoundException;
import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {
    private TransactionRepository repository;
    private TransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(TransactionRepository.class);
        service = new TransactionServiceImpl(repository);
    }

    @Test
    void getTransactionsByCustomerId_returnsList() {
        Transaction t = new Transaction();
        t.setCustomerId("1");
        t.setTransactionDate(Instant.now());
        when(repository.findByCustomerId("1")).thenReturn(List.of(t));

        List<TransactionDTO> result = service.getTransactionsByCustomerId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTransactionsByCustomerId_noTransactions_throwsNotFound() {
        when(repository.findByCustomerId("1")).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class, () -> service.getTransactionsByCustomerId(1L));
    }

    @Test
    void getTransactionsByCustomerIdAndDateRange_invalidRange_throwsException() {
        LocalDate start = LocalDate.of(2025, 9, 10);
        LocalDate end = LocalDate.of(2025, 9, 1);
        assertThrows(InvalidDateRangeException.class,
                () -> service.getTransactionsByCustomerIdAndDateRange(1L, start, end));
    }
}
