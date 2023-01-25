package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.exception.ShipmentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.cocosmaj.BellBooks.service.shipment.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@CrossOrigin
@RestController
public class ShipmentController {


    private ShipmentService shipmentService;

    public ShipmentController( ShipmentService shipmentService){
        this.shipmentService = shipmentService;
    }

    @PostMapping("/addShipment")
    public ResponseEntity addShipment(@RequestBody Shipment shipment){
            return ResponseEntity.ok(shipmentService.addShipment(shipment));
    }

    @PutMapping("/updateShipment")
    public ResponseEntity updateShipment(@RequestBody Shipment shipment){
        try {
            return ResponseEntity.ok(shipmentService.updateShipment(shipment));
        } catch (ShipmentNotFoundException e) {
            throw new RuntimeException(e);
        }
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
    public ResponseEntity getAllShipmentsByRecipient(@RequestParam Long id){
        return ResponseEntity.ok(shipmentService.getAllShipmentsByRecipient(id));
    }

    //get all shipments by date

    @DeleteMapping("/deleteShipment")
    public ResponseEntity deleteShipment(@RequestParam Long id){
        shipmentService.deleteShipment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllShipmentsByRecipientId")
    public ResponseEntity deleteShipmentsByRecipient(@RequestParam Long id){
        this.shipmentService.deleteShipmentsByRecipient(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getShipmentsByDate")
    public ResponseEntity getShipmentsByDate(@RequestParam String date){
        return ResponseEntity.ok(shipmentService.getShipmentsByDate(LocalDate.parse(date)));
    }



}
