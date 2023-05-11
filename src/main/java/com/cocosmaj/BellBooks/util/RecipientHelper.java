package com.cocosmaj.BellBooks.util;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;

import java.util.Optional;

public class RecipientHelper {

    public static Recipient extractRecipient(Optional<Recipient> recipient) throws RecipientNotFoundException{
        if (recipient.isPresent()){
            return recipient.get();
        } else {
            throw new RecipientNotFoundException();
        }
    }
}
