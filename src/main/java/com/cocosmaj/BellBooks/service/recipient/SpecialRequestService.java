package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.repository.SpecialRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialRequestService {


    private SpecialRequestRepository specialRequestRepository;

    public SpecialRequestService( SpecialRequestRepository specialRequestRepository){
        this.specialRequestRepository = specialRequestRepository;
    }
}
