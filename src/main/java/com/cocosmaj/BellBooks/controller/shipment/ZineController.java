package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.model.shipment.Zine;
import com.cocosmaj.BellBooks.service.shipment.ZineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ZineController {

    @Autowired
    private ZineService zineService;

    public ZineController(@Autowired ZineService zineService){
        this.zineService = zineService;
    }

    @PostMapping("/addZine")
    public ResponseEntity addZine(@RequestBody Zine zine){
        return ResponseEntity.ok(this.zineService.addZine(zine));
    }

    @GetMapping("/getAllZines")
    public ResponseEntity getAllZines(){
        return ResponseEntity.ok(zineService.getAllZines());
    }

}
