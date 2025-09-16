package com.api.customer.controller;

import com.api.customer.dto.TransactionDTO;
import com.api.customer.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {
    @Test
    void create_returnsCreated() {
        TransactionService service = mock(TransactionService.class);
        TransactionDTO dto = TransactionDTO.builder().id(1L).customerId("1").amount(100.0).build();
        when(service.createTransaction(dto)).thenReturn(dto);
        TransactionController controller = new TransactionController(service);
        ResponseEntity<TransactionDTO> response = controller.create(dto);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getByCustomer_returnsList() {
        TransactionService service = mock(TransactionService.class);
        when(service.getTransactionsByCustomerId(1L))
            .thenReturn(List.of(TransactionDTO.builder().id(1L).customerId("1").amount(50.0).build()));

        TransactionController controller = new TransactionController(service);
        ResponseEntity<List<TransactionDTO>> response = controller.getByCustomer(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getByCustomerAndDateRange_returnsList() {
        TransactionService service = mock(TransactionService.class);
        when(service.getTransactionsByCustomerIdAndDateRange(anyLong(), any(), any()))
            .thenReturn(List.of(TransactionDTO.builder().id(1L).customerId("1").amount(75.0).build()));

        TransactionController controller = new TransactionController(service);
        ResponseEntity<List<TransactionDTO>> response = controller.getByCustomerAndDateRange(
                1L, LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 30));

        assertEquals(200, response.getStatusCodeValue());
    }
}
