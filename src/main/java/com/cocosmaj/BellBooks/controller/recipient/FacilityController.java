package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.service.recipient.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    public FacilityController(@Autowired FacilityService facilityService){
        this.facilityService = facilityService;
    }

    @PostMapping("/addFacility")
    public ResponseEntity addFacility(@RequestBody Facility facility){
        return ResponseEntity.ok(facilityService.addFacility(facility));
    }

    @GetMapping("/getAllFacilities")
    public ResponseEntity getAllFacilities(){
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }
}
