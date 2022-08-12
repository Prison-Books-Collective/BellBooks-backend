package com.cocosmaj.BellBooks.service;

import com.cocosmaj.BellBooks.model.Facility;
import com.cocosmaj.BellBooks.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    public FacilityService(@Autowired FacilityRepository facilityRepository){
        this.facilityRepository = facilityRepository;
    }
    public Facility addFacility(Facility facility) {

        return facilityRepository.save(facility);
    }
}
