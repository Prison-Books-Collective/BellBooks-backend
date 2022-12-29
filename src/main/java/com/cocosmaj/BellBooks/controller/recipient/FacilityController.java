package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.exception.FacilityNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.service.recipient.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/getFacility")
    public ResponseEntity getFacilityById(@RequestParam Long id){
        try {
            return ResponseEntity.ok(facilityService.getFacilityById(id));
        } catch (FacilityNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteFacility")
    public ResponseEntity deleteFacility(@RequestParam Long id){
        try {
            facilityService.deleteFacility(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (FacilityNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateFacility")
    public ResponseEntity updateFacility(@RequestBody Facility facility){
        try {
            return ResponseEntity.ok(facilityService.updateFacility(facility));
        } catch (FacilityNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
