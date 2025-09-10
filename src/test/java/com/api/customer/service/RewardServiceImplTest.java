package com.api.customer.service;

import com.api.customer.dto.RewardSummaryDTO;
import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.serviceImpl.RewardServiceImpl;
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
public class RewardServiceImplTest {

    @Test
    public void testGetAllTimeSummary() {
        TransactionRepository repo = Mockito.mock(TransactionRepository.class);
        Transaction t = new Transaction(1L, 120.0, LocalDateTime.parse("2024-07-01T00:00:00"));
        t.setId(11L);
        when(repo.findByCustomerId(1L)).thenReturn(Arrays.asList(t));
        RewardServiceImpl svc = new RewardServiceImpl(repo);
        RewardSummaryDTO dto = svc.getAllTimeSummary(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getCustomerId());
        assertTrue(dto.getTotalReward() > 0);
    }
}
