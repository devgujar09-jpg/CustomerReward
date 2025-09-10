package com.api.customer.service;

import com.api.customer.dto.TransactionDTO;
import com.api.customer.exception.InvalidDateRangeException;
import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class TransactionServiceDateRangeTest {

    @Test
    public void invalidDateRangeThrows() {
        TransactionRepository repo = Mockito.mock(TransactionRepository.class);
        TransactionServiceImpl svc = new TransactionServiceImpl(repo);
        LocalDate start = LocalDate.of(2024, 7, 2);
        LocalDate end = LocalDate.of(2024, 7, 1);

        assertThrows(InvalidDateRangeException.class,
                () -> svc.getTransactionsByCustomerIdAndDateRange(1L, start, end));
    }

    @Test
    public void inclusiveEdgesAreUsed() {
        TransactionRepository repo = Mockito.mock(TransactionRepository.class);
        TransactionServiceImpl svc = new TransactionServiceImpl(repo);

        LocalDate start = LocalDate.of(2024, 7, 1);
        LocalDate end = LocalDate.of(2024, 7, 1);

        // Prepare a transaction exactly at end-of-day to ensure inclusion
        LocalDateTime edgeTime = LocalDateTime.of(2024, 7, 1, 23, 59, 59, 999_000_000);
        Transaction tx = new Transaction(1L, 100.0, edgeTime);
        tx.setId(42L);

        when(repo.findByCustomerIdAndDateBetween(
                Mockito.eq(1L),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class))
        ).thenReturn(Collections.singletonList(tx));

        List<TransactionDTO> results = svc.getTransactionsByCustomerIdAndDateRange(1L, start, end);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(42L, results.get(0).getTransactionId());

        // Verify the repository was called with [start at 00:00, end at LocalTime.MAX]
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(repo).findByCustomerIdAndDateBetween(Mockito.eq(1L), startCaptor.capture(), endCaptor.capture());

        assertEquals(start.atStartOfDay(), startCaptor.getValue());
        assertEquals(end.atTime(LocalTime.MAX), endCaptor.getValue());
    }
}
