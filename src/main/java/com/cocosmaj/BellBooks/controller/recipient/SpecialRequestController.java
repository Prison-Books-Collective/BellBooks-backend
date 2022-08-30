package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.service.recipient.SpecialRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SpecialRequestController {

    @Autowired
    private SpecialRequestService specialRequestService;

    public SpecialRequestController(@Autowired SpecialRequestService specialRequestService){
        this.specialRequestService = specialRequestService;
    }
}
