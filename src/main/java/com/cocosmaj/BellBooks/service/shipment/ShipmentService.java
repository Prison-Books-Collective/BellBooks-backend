package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.cocosmaj.BellBooks.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public ShipmentService(@Autowired ShipmentRepository shipmentRepository){
        this.shipmentRepository = shipmentRepository;
    }


    public Shipment addShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }
}
