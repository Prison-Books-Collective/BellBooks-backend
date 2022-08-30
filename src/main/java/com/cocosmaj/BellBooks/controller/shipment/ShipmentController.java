package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.cocosmaj.BellBooks.service.shipment.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
