package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.exception.ShipmentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.cocosmaj.BellBooks.service.shipment.ShipmentService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentService shipmentService;

    @Nested
    class GetShipment {

        @Test
        void returns200_whenFound() throws Exception {
            Shipment shipment = new Shipment();
            shipment.setId(1L);
            shipment.setDate(LocalDate.of(2024, 6, 15));

            when(shipmentService.getShipment(1L)).thenReturn(shipment);

            mockMvc.perform(get("/getShipment").param("id", "1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void returns400_whenNotFound() throws Exception {
            when(shipmentService.getShipment(99L)).thenThrow(new ShipmentNotFoundException());

            mockMvc.perform(get("/getShipment").param("id", "99"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class UpdateShipment {

        @Test
        void throwsException_whenShipmentNotFound_documentsBug() throws Exception {
            when(shipmentService.updateShipment(any())).thenThrow(new ShipmentNotFoundException());

            // BUG: controller wraps ShipmentNotFoundException in RuntimeException
            // instead of returning 400 like the other controllers do.
            // This results in a 500 at runtime. MockMvc surfaces it as NestedServletException.
            assertThrows(NestedServletException.class, () ->
                mockMvc.perform(put("/updateShipment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":99}"))
            );
        }
    }

    @Nested
    class GetShipmentsByDate {

        @Test
        void returns200_withValidDate() throws Exception {
            when(shipmentService.getShipmentsByDate(LocalDate.of(2024, 6, 15)))
                .thenReturn(List.of());

            mockMvc.perform(get("/getShipmentsByDate").param("date", "2024-06-15"))
                .andExpect(status().is(200));
        }

        @Test
        void throwsException_withInvalidDateFormat_documentsBug() throws Exception {
            // BUG: controller does not validate date format before parsing.
            // LocalDate.parse throws DateTimeParseException which is unhandled,
            // resulting in a 500 at runtime.
            assertThrows(NestedServletException.class, () ->
                mockMvc.perform(get("/getShipmentsByDate").param("date", "not-a-date"))
            );
        }
    }

    @Nested
    class DeleteShipment {

        @Test
        void returns200_always() throws Exception {
            mockMvc.perform(delete("/deleteShipment").param("id", "1"))
                .andExpect(status().is(200));

            verify(shipmentService).deleteShipment(1L);
        }
    }
}
