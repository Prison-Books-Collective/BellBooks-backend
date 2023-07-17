package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.exception.FacilityNotFoundException;
import com.cocosmaj.BellBooks.model.enums.State;
import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.repository.recipient.FacilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {


    private FacilityRepository facilityRepository;

    public FacilityService( FacilityRepository facilityRepository){
        this.facilityRepository = facilityRepository;
    }
    public Facility addFacility(Facility facility) {

        return facilityRepository.save(facility);
    }

    public List<Facility> getAllFacilities() {
        return (List) facilityRepository.findAllByOrderByName();
    }

    public Facility getFacilityById(Long id) throws FacilityNotFoundException {
        Optional<Facility> byId = facilityRepository.findById(id);
        if (byId.isPresent()){
            return byId.get();
        } else {
            throw new FacilityNotFoundException();
        }
    }

    public void deleteFacility(Long id) throws FacilityNotFoundException {
        getFacilityById(id);
        facilityRepository.deleteById(id);

    }

    public Facility updateFacility(Facility facility) throws FacilityNotFoundException{
        getFacilityById(facility.getId());
        return facilityRepository.save(facility);
    }

    public List<Facility> getFacilityByNameAndState(String name, State state) {
        List<Facility> allByNameAndState = facilityRepository.findAllByNameAndState(name, state);
        return allByNameAndState;
    }
}
