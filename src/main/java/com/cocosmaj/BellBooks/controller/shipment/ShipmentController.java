package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.exception.ShipmentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.cocosmaj.BellBooks.service.shipment.ShipmentService;
import org.hibernate.TransientObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    public ShipmentController(@Autowired ShipmentService shipmentService){
        this.shipmentService = shipmentService;
    }

    @PostMapping("/addShipment")
    public ResponseEntity addShipment(@RequestBody Shipment shipment){
            return ResponseEntity.ok(shipmentService.addShipment(shipment));

    }

    @GetMapping("/getShipment")
    public ResponseEntity getShipment(@RequestParam Long id){
        try {
            return ResponseEntity.ok(shipmentService.getShipment(id));
         } catch (ShipmentNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getAllShipmentsByRecipient")
    public ResponseEntity getAllShipmentsByRecipient(@RequestParam Long recipientId){
        return ResponseEntity.ok(shipmentService.getAllShipmentsByRecipient(recipientId));
    }

    //get all shipments by date

    @DeleteMapping("/deleteShipment")
    public ResponseEntity deleteShipment(@RequestParam Long id){
        shipmentService.deleteShipment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
