package com.cocosmaj.BellBooks.repository.recipient;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface RecipientRepository extends CrudRepository<Recipient, Long> {
    Optional<Recipient> findByAssignedId(String assignedId);

    List<Recipient> findAllByFirstNameContainingAndLastNameContaining(String firstName, String lastName);
}
