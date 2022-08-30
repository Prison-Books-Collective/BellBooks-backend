package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.service.recipient.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class RecipientController {

    @Autowired
    private RecipientService recipientService;

    public RecipientController(@Autowired RecipientService recipientService){
        this.recipientService = recipientService;
    }

    @PostMapping("/addRecipient")
    public ResponseEntity addRecipient(@RequestBody Recipient recipient){
        return ResponseEntity.ok(this.recipientService.addRecipient(recipient));
    }

    @PutMapping("/updateRecipient")
    public ResponseEntity updateRecipient(@RequestBody Recipient recipient){
        try {
            return ResponseEntity.ok(this.recipientService.updateRecipient(recipient));
        } catch (RecipientNotFoundException exception) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getRecipient")
    public ResponseEntity getRecipient(@RequestParam Long recipientId) {
        try {
            return ResponseEntity.ok(this.recipientService.getRecipient(recipientId));
        } catch (RecipientNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}
