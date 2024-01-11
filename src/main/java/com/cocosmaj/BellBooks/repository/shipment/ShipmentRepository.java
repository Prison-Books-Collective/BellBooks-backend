package com.cocosmaj.BellBooks.repository.shipment;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
public interface ShipmentRepository extends CrudRepository<Shipment, Long> {
    List<Shipment> findAllByRecipientId(Long recipientId);

    void deleteByRecipientId(Long id);

    List<Shipment> findAllByDate(LocalDate date);

    Long countByDateBetween(LocalDate date1, LocalDate date2);
}
