package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.exception.ShipmentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.cocosmaj.BellBooks.repository.shipment.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {


    private final ShipmentRepository shipmentRepository;

    @SuppressWarnings("unused")
    public ShipmentService(ShipmentRepository shipmentRepository){
        this.shipmentRepository = shipmentRepository;
    }

    public Shipment addShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public Shipment getShipment(Long id) throws ShipmentNotFoundException {
        Optional<Shipment> byId = shipmentRepository.findById(id);
        if (byId.isPresent()){
            return byId.get();
        } else {
            throw new ShipmentNotFoundException();
        }
    }

    public List<Shipment> getAllShipmentsByRecipient(Long id) {
        return shipmentRepository.findAllByRecipientId(id);
    }

    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }

    public void deleteShipmentsByRecipient(Long id) {
        this.shipmentRepository.deleteByRecipientId(id);

    }

    public List<Shipment> getShipmentsByDate(LocalDate date) {
        return this.shipmentRepository.findAllByDate(date);
    }

    public Shipment updateShipment(Shipment shipment) throws ShipmentNotFoundException {
        getShipment(shipment.getId());
        return this.shipmentRepository.save(shipment);
    }

    public Long getShipmentCountBetweenDates(LocalDate date1, LocalDate date2) {
        return shipmentRepository.countByDateBetween(date1, date2);
    }
}
