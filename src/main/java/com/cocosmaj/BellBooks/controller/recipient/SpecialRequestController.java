package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.SpecialRequest;
import com.cocosmaj.BellBooks.service.recipient.SpecialRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class SpecialRequestController {

    private final SpecialRequestService specialRequestService;

    public SpecialRequestController(SpecialRequestService specialRequestService) {
        this.specialRequestService = specialRequestService;
    }

    @PostMapping("/addSpecialRequest")
    public ResponseEntity<SpecialRequest> addSpecialRequest(@RequestBody SpecialRequest specialRequest) {
        try {
            return ResponseEntity.ok(this.specialRequestService.addSpecialRequest(specialRequest));
        } catch (RecipientNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllSpecialRequests")
    public ResponseEntity<List<SpecialRequest>> getAllSpecialRequests(){
        return ResponseEntity.ok(specialRequestService.getAllSpecialRequests());
    }

    //update

    //delete

    //get special request by inmate

    //get special request by id

    //get all
}
