package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.repository.SpecialRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialRequestService {

    @Autowired
    private SpecialRequestRepository specialRequestRepository;

    public SpecialRequestService(@Autowired SpecialRequestRepository specialRequestRepository){
        this.specialRequestRepository = specialRequestRepository;
    }
}
