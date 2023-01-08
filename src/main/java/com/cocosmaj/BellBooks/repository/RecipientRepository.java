package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipientRepository extends CrudRepository<Recipient, Long> {
    public Optional<Recipient> findByAssignedId(String assignedId);

    List<Recipient> findAllByFirstNameContainingAndLastNameContaining(String firstName, String lastName);
}
