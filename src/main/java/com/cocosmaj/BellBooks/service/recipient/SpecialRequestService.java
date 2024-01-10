package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.model.recipient.SpecialRequest;
import com.cocosmaj.BellBooks.repository.recipient.RecipientRepository;
import com.cocosmaj.BellBooks.repository.recipient.SpecialRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialRequestService {

    private final SpecialRequestRepository specialRequestRepository;
    private final RecipientRepository recipientRepository;

    @SuppressWarnings("unused")
    public SpecialRequestService(SpecialRequestRepository specialRequestRepository, RecipientRepository recipientRepository){
        this.specialRequestRepository = specialRequestRepository;
        this.recipientRepository = recipientRepository;
    }

    public SpecialRequest addSpecialRequest(SpecialRequest specialRequest) throws RecipientNotFoundException {
        Recipient databaseRecipient = recipientRepository
            .findById(specialRequest.getRecipient().getId())
            .orElseThrow(RecipientNotFoundException::new);
        SpecialRequest savedSpecialRequest = specialRequestRepository.save(specialRequest);
        databaseRecipient.getSpecialRequests().add(savedSpecialRequest);
        recipientRepository.save(databaseRecipient);
        return savedSpecialRequest;
    }

    public List<SpecialRequest> getAllSpecialRequests() {
        return (List) specialRequestRepository.findAll();
    }
}
