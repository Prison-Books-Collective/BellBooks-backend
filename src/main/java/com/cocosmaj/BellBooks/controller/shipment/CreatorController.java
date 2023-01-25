package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.service.shipment.CreatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CreatorController {

    private CreatorService creatorService;

    public CreatorController(CreatorService creatorService){
        this.creatorService = creatorService;
    }

    @DeleteMapping("/deleteCreator")
    public ResponseEntity deleteCreator(@RequestParam Long id){
        this.creatorService.deleteCreator(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
