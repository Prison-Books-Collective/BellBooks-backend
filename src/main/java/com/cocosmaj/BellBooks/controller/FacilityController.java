package com.cocosmaj.BellBooks.controller;

import com.cocosmaj.BellBooks.model.Facility;
import com.cocosmaj.BellBooks.service.FacilityService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
