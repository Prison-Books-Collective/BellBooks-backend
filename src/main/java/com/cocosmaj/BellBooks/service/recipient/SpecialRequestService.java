package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.model.recipient.SpecialRequest;
import com.cocosmaj.BellBooks.repository.recipient.RecipientRepository;
import com.cocosmaj.BellBooks.repository.recipient.SpecialRequestRepository;
import com.cocosmaj.BellBooks.util.RecipientHelper;
import org.springframework.stereotype.Service;

@Service
public class SpecialRequestService {

    private SpecialRequestRepository specialRequestRepository;
    private RecipientRepository recipientRepository;

    public SpecialRequestService(SpecialRequestRepository specialRequestRepository, RecipientRepository recipientRepository){
        this.specialRequestRepository = specialRequestRepository;
        this.recipientRepository = recipientRepository;
    }

    public SpecialRequest addSpecialRequest(SpecialRequest specialRequest) throws RecipientNotFoundException {
        Recipient databaseRecipient = RecipientHelper.extractRecipient(recipientRepository.findById(specialRequest.getRecipient().getId()));
        SpecialRequest savedSpecialRequest = specialRequestRepository.save(specialRequest);
        databaseRecipient.getSpecialRequests().add(savedSpecialRequest);
        recipientRepository.save(databaseRecipient);
        return savedSpecialRequest;
    }
}
