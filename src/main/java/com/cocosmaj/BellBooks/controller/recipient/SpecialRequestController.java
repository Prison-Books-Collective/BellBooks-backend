package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.service.recipient.SpecialRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SpecialRequestController {


    private SpecialRequestService specialRequestService;

    public SpecialRequestController( SpecialRequestService specialRequestService){
        this.specialRequestService = specialRequestService;
    }

    //add

    //update

    //delete

    //get special request by inmate

    //get special request by id

    //get all
}
