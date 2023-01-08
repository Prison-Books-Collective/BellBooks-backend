package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.repository.RecipientRepository;
import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Recipient updateRecipient(Recipient recipient) throws RecipientNotFoundException {
        getRecipient(recipient.getId());
        return recipientRepository.save(recipient);
    }

    public Recipient getRecipient(Long recipientId) throws RecipientNotFoundException {
        Optional<Recipient> byId = recipientRepository.findById(recipientId);
        if (byId.isEmpty()){
            throw new RecipientNotFoundException();
        } else {
            return byId.get();
        }
    }

    public void deleteRecipient(Long id) throws RecipientNotFoundException{
        getRecipient(id);
        recipientRepository.deleteById(id);
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
