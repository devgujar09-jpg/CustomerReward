package com.api.customer.controller;

import com.api.customer.dto.RewardSummaryDTO;
import com.api.customer.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardControllerTest {
    @Test
    void getAllTimeRewards_returnsOk() {
        RewardService service = mock(RewardService.class);
        RewardSummaryDTO dto = RewardSummaryDTO.builder().customerId(1L).totalPoints(50).build();
        when(service.getAllTimeSummary(1L)).thenReturn(dto);

        RewardController controller = new RewardController(service);
        ResponseEntity<RewardSummaryDTO> response = controller.getAllTimeRewards(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(50, response.getBody().getTotalPoints());
    }

    @Test
    void getAllCustomersMonthlyRewards_returnsList() {
        RewardService service = mock(RewardService.class);
        when(service.getAllCustomersMonthlySummary("2025-09"))
                .thenReturn(List.of(RewardSummaryDTO.builder().customerId(1L).totalPoints(20).build()));
        RewardController controller = new RewardController(service);
        ResponseEntity<List<RewardSummaryDTO>> response = controller.getAllCustomersMonthlyRewards("2025-09");
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }
}
