package com.api.customer.controller;

import com.api.customer.dto.RewardSummaryDTO;
import com.api.customer.service.RewardService;
import com.api.customer.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Test
    void getAllTimeRewards_ok() throws Exception {
        when(rewardService.getAllTimeSummary(1L)).thenReturn(new RewardSummaryDTO(1L, null, 120, java.util.Collections.emptyList()));
        mockMvc.perform(get("/api/customers/1/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalReward").value(120));
    }


        @Test
    void getAllTimeRewards_notFound_returns404() throws Exception {
        org.mockito.Mockito.when(rewardService.getAllTimeSummary(99L))
                .thenThrow(new NotFoundException("Customer not found or no transactions"));
        mockMvc.perform(get("/api/customers/99/rewards"))
                .andExpect(status().isNotFound());
    }

}
