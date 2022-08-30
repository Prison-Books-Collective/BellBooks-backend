package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.repository.RecipientRepository;
import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipientService {
    @Autowired
    private RecipientRepository recipientRepository;

    public RecipientService(@Autowired RecipientRepository recipientRepository){
        this.recipientRepository = recipientRepository;
    }

    public Recipient addRecipient(Recipient recipient) {
        return this.recipientRepository.save(recipient);
    }

    public Recipient updateRecipient(Recipient recipient) throws RecipientNotFoundException {
        try {
            getRecipient(recipient.getId());
            return recipientRepository.save(recipient);
        } catch (RecipientNotFoundException exception){
            throw exception;
        }
    }

    public Recipient getRecipient(Long recipientId) throws RecipientNotFoundException {
        Optional<Recipient> byId = recipientRepository.findById(recipientId);
        if (byId.isEmpty()){
            throw new RecipientNotFoundException();
        } else {
            return byId.get();
        }
    }
}
