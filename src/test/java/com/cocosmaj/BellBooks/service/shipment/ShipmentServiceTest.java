package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.exception.ShipmentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.cocosmaj.BellBooks.repository.shipment.ShipmentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService service;

    @Nested
    class GetShipment {

        @Test
        void returnsShipment_whenFound() throws ShipmentNotFoundException {
            Shipment shipment = new Shipment();
            shipment.setId(1L);

            when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

            Shipment result = service.getShipment(1L);

            assertSame(shipment, result);
        }

        @Test
        void throwsException_whenNotFound() {
            when(shipmentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ShipmentNotFoundException.class, () -> service.getShipment(99L));
        }
    }

    @Nested
    class UpdateShipment {

        @Test
        void savesShipment_whenItExists() throws ShipmentNotFoundException {
            Shipment shipment = new Shipment();
            shipment.setId(1L);
            shipment.setDate(LocalDate.of(2024, 1, 15));

            when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
            when(shipmentRepository.save(shipment)).thenReturn(shipment);

            Shipment result = service.updateShipment(shipment);

            assertSame(shipment, result);
            verify(shipmentRepository).save(shipment);
        }

        @Test
        void throwsException_whenShipmentDoesNotExist() {
            Shipment shipment = new Shipment();
            shipment.setId(99L);

            when(shipmentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ShipmentNotFoundException.class, () -> service.updateShipment(shipment));
            verify(shipmentRepository, never()).save(any());
        }
    }

    @Nested
    class DeleteShipment {

        @Test
        void deletesWithoutExistenceCheck() {
            service.deleteShipment(1L);

            verify(shipmentRepository).deleteById(1L);
        }
    }

    @Nested
    class GetShipmentsByDate {

        @Test
        void delegatesToRepository() {
            LocalDate date = LocalDate.of(2024, 6, 15);
            List<Shipment> expected = List.of(new Shipment());

            when(shipmentRepository.findAllByDate(date)).thenReturn(expected);

            List<Shipment> result = service.getShipmentsByDate(date);

            assertSame(expected, result);
        }
    }

    @Nested
    class GetShipmentCountBetweenDates {

        @Test
        void delegatesToRepository() {
            LocalDate start = LocalDate.of(2024, 1, 1);
            LocalDate end = LocalDate.of(2024, 12, 31);

            when(shipmentRepository.countByDateBetween(start, end)).thenReturn(42L);

            Long result = service.getShipmentCountBetweenDates(start, end);

            assertEquals(42L, result);
        }
    }
}
