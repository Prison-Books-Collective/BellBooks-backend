package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.model.shipment.Note;
import com.cocosmaj.BellBooks.controller.repository.shipment.NoteRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public Note addNote(Note note){
        return this.noteRepository.save(note);
    }

}
