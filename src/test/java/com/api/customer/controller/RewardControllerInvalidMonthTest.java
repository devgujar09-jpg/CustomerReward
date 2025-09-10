package com.api.customer.controller;

import com.api.customer.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RewardController.class)
public class RewardControllerInvalidMonthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Test
    void invalidMonthFormat_returns400() throws Exception {
        mockMvc.perform(get("/api/customers/1/rewards")
                .param("month", "2024/07")) // invalid format, expects yyyy-MM
                .andExpect(status().isBadRequest());
    }
}
