package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.repository.PackageContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageContentService {

    @Autowired
    private PackageContentRepository packageContentRepository;

    public PackageContentService(@Autowired PackageContentRepository packageContentRepository){
        this.packageContentRepository = packageContentRepository;
    }
}
