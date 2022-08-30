package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Facility> getAllFacilities() {
        return (List) facilityRepository.findAll();
    }
}
