package com.api.customer.service;

import com.api.customer.dto.TransactionDTO;
import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Auto-generated documentation.
 */
/**
 * See README for details.
 */
public class TransactionServiceImplTest {

    @Test
    public void testGetTransactionsByCustomerId() {
        TransactionRepository repo = Mockito.mock(TransactionRepository.class);
        Transaction t = new Transaction(1L, 120.0, LocalDateTime.parse("2024-07-01T00:00:00"));
        t.setId(10L);
        when(repo.findByCustomerId(1L)).thenReturn(Arrays.asList(t));
        TransactionServiceImpl svc = new TransactionServiceImpl(repo);
        List<TransactionDTO> res = svc.getTransactionsByCustomerId(1L);
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(10L, res.get(0).getTransactionId());
    }
}
