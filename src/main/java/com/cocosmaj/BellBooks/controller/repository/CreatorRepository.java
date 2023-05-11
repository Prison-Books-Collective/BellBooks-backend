package com.cocosmaj.BellBooks.controller.repository;

import com.cocosmaj.BellBooks.model.shipment.Author;
import com.cocosmaj.BellBooks.model.shipment.Creator;
import com.cocosmaj.BellBooks.model.shipment.AuthorGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, Long> {

    Optional<Author> findByFirstNameAndLastName(String first, String last);

    Optional<AuthorGroup> findByName(String name);
}
