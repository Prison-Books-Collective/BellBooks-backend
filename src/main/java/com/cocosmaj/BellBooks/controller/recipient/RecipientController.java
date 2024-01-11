package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.service.recipient.RecipientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
public class RecipientController {


    private final RecipientService recipientService;

    public RecipientController(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @PostMapping("/addRecipient")
    public ResponseEntity<Recipient> addRecipient(@RequestBody Recipient recipient) {
        return ResponseEntity.ok(this.recipientService.addRecipient(recipient));
    }

    @PutMapping("/updateRecipient")
    public ResponseEntity<Recipient> updateRecipient(@RequestBody Recipient recipient) {
        try {
            return ResponseEntity.ok(this.recipientService.updateRecipient(recipient));
        } catch (RecipientNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getRecipients")
    public ResponseEntity<List<Recipient>> getRecipients(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok(this.recipientService.getRecipients(firstName, lastName));
    }

    @GetMapping("/getRecipient")
    public ResponseEntity<Recipient> getRecipient(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(this.recipientService.getRecipientById(id));
        } catch (RecipientNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllRecipients")
    public ResponseEntity<List<Recipient>> getAllRecipients() {
        return ResponseEntity.ok(this.recipientService.getAllRecipients());
    }

    @GetMapping("/getRecipientByAssignedId")
    public ResponseEntity<Recipient> getRecipientByAssignedId(@RequestParam String assignedId) {
        try {
            return ResponseEntity.ok(this.recipientService.getRecipientByAssignedId(assignedId));
        } catch (RecipientNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getRecipientLocation")
    public ResponseEntity<String> getRecipientLocation(@RequestParam String id) throws IOException {
        return ResponseEntity.ok(this.recipientService.getRecipientLocation(id));
    }

    @DeleteMapping("/deleteRecipient")
    public ResponseEntity<Void> deleteRecipient(@RequestParam Long id) {
        try {
            this.recipientService.deleteRecipient(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RecipientNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
