package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.service.shipment.PackageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class PackageContentController {

    @Autowired
    private PackageContentService packageContentService;

    public PackageContentController(@Autowired PackageContentService packageContentService){
        this.packageContentService = packageContentService;
    }
}
