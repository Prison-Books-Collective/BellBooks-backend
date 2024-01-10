package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.model.shipment.Note;
import com.cocosmaj.BellBooks.service.shipment.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    @PostMapping("/addNote")
    public ResponseEntity<Note> addNote(@RequestBody Note note){
        return ResponseEntity.ok(noteService.addNote(note));
    }

}
