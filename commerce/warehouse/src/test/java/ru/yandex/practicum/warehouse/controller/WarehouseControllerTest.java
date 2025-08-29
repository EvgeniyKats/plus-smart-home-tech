package ru.yandex.practicum.warehouse.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.warehouse.service.WarehouseService;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class WarehouseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WarehouseService warehouseService;

    @Test
    void getAddress_shouldBeOneRequest() throws Exception {
        mockMvc.perform(get("/api/v1/warehouse/address"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/warehouse/address"))
                .andExpect(status().isOk());

        Mockito.verify(warehouseService, times(1))
                .getAddress();
    }
}