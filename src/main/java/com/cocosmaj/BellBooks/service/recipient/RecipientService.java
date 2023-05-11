package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.controller.repository.RecipientRepository;
import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.util.RecipientHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipientService {

    private RecipientRepository recipientRepository;

    public RecipientService( RecipientRepository recipientRepository){
        this.recipientRepository = recipientRepository;
    }

    public Recipient addRecipient(Recipient recipient) {
        return this.recipientRepository.save(recipient);
    }

    public Recipient updateRecipient(Recipient newRecipientInfo) throws RecipientNotFoundException {
        Recipient databaseRecipient = RecipientHelper.extractRecipient(recipientRepository.findById(newRecipientInfo.getId()));
        if (!databaseRecipient.getAssignedId().equals(newRecipientInfo.getAssignedId())) {
            databaseRecipient.setAssignedId(newRecipientInfo.getAssignedId());
        }
        if (!databaseRecipient.getFirstName().equals(newRecipientInfo.getFirstName())) {
            databaseRecipient.setFirstName(newRecipientInfo.getFirstName());
        }
        if (!databaseRecipient.getLastName().equals(newRecipientInfo.getLastName())) {
            databaseRecipient.setLastName(newRecipientInfo.getLastName());
        }
        return recipientRepository.save(databaseRecipient);
    }

    public void deleteRecipient(Long id) throws RecipientNotFoundException{
        getRecipientById(id);
        recipientRepository.deleteById(id);
    }

    public Recipient getRecipientById(Long id) throws RecipientNotFoundException {
        return RecipientHelper.extractRecipient(recipientRepository.findById(id));
    }

    public Recipient getRecipientByAssignedId(String assignedId) throws RecipientNotFoundException {
        Optional<Recipient> byId = recipientRepository.findByAssignedId(assignedId);
        if (byId.isEmpty()){
            throw new RecipientNotFoundException();
        } else {
            return byId.get();
        }
    }

    public List<Recipient> getAllRecipients() {
        return (List) recipientRepository.findAll();
    }

    public List<Recipient> getRecipients(String firstName, String lastName) {
        return recipientRepository.findAllByFirstNameContainingAndLastNameContaining(firstName, lastName);
    }
}
