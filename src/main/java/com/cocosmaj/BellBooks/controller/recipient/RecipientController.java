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


    private RecipientService recipientService;

    public RecipientController( RecipientService recipientService){
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

    @GetMapping("getRecipients")
    public ResponseEntity getRecipients(@RequestParam String firstName, @RequestParam String lastName){
        return ResponseEntity.ok(this.recipientService.getRecipients(firstName, lastName));
    }
    @GetMapping("/getRecipient")
    public ResponseEntity getRecipient(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(this.recipientService.getRecipient(id));
        } catch (RecipientNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllRecipients")
    public ResponseEntity getAllRecipients(){
        return ResponseEntity.ok(this.recipientService.getAllRecipients());
    }

    @GetMapping("/getRecipientByAssignedId")
    public ResponseEntity getRecipientByAssignedId(@RequestParam String assignedId){
        try {
            return ResponseEntity.ok(this.recipientService.getRecipientByAssignedId(assignedId));
        } catch (RecipientNotFoundException exception){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteRecipient")
    public ResponseEntity deleteRecipient(@RequestParam Long id){
        try{
            this.recipientService.deleteRecipient(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RecipientNotFoundException exception){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
