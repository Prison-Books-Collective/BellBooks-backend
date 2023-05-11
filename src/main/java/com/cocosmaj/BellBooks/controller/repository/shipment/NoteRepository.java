package com.cocosmaj.BellBooks.controller.repository.shipment;

import com.cocosmaj.BellBooks.model.shipment.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {
}
