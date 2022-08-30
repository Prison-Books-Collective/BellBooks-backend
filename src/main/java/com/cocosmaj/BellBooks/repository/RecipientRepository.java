package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends CrudRepository<Recipient, Long> {
}
