package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.exception.FacilityNotFoundException;
import com.cocosmaj.BellBooks.model.enums.State;
import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.service.recipient.FacilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping("/addFacility")
    public ResponseEntity<Facility> addFacility(@RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.addFacility(facility));
    }

    @GetMapping("/getAllFacilities")
    public ResponseEntity<List<Facility>> getAllFacilities() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    @GetMapping("/getFacility")
    public ResponseEntity<Facility> getFacilityById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(facilityService.getFacilityById(id));
        } catch (FacilityNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getFacilityByName")
    public ResponseEntity<List<Facility>> getFacilityByNameAndState(@RequestParam String name, @RequestParam State state) {
        return ResponseEntity.ok(facilityService.getFacilityByNameAndState(name, state));
    }

    @DeleteMapping("/deleteFacility")
    public ResponseEntity<Void> deleteFacility(@RequestParam Long id) {
        try {
            facilityService.deleteFacility(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FacilityNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateFacility")
    public ResponseEntity<Facility> updateFacility(@RequestBody Facility facility) {
        try {
            return ResponseEntity.ok(facilityService.updateFacility(facility));
        } catch (FacilityNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
