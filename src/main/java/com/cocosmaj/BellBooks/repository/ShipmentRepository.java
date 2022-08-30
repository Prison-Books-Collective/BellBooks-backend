package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, Long> {
}
