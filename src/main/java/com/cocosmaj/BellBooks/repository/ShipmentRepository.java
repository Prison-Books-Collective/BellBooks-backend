package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShipmentRepository extends CrudRepository<Shipment, Long> {
    public List<Shipment> findAllByRecipientId(Long recipientId);

    void deleteByRecipientId(Long id);

}
