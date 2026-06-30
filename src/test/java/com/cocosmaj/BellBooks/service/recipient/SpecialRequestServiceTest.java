package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.model.recipient.SpecialRequest;
import com.cocosmaj.BellBooks.repository.recipient.RecipientRepository;
import com.cocosmaj.BellBooks.repository.recipient.SpecialRequestRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialRequestServiceTest {

    @Mock
    private SpecialRequestRepository specialRequestRepository;

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private SpecialRequestService service;

    @Nested
    class AddSpecialRequest {

        @Test
        void savesRequest_andAddsToRecipientList() throws RecipientNotFoundException {
            Recipient recipient = new Recipient();
            recipient.setId(1L);
            recipient.setSpecialRequests(new ArrayList<>());

            SpecialRequest request = new SpecialRequest();
            request.setRequest("Need vocational books");
            request.setRecipient(recipient);

            SpecialRequest savedRequest = new SpecialRequest();
            savedRequest.setId(10L);
            savedRequest.setRequest("Need vocational books");
            savedRequest.setRecipient(recipient);

            when(recipientRepository.findById(1L)).thenReturn(Optional.of(recipient));
            when(specialRequestRepository.save(request)).thenReturn(savedRequest);
            when(recipientRepository.save(recipient)).thenReturn(recipient);

            SpecialRequest result = service.addSpecialRequest(request);

            assertSame(savedRequest, result);
            assertTrue(recipient.getSpecialRequests().contains(savedRequest));
            verify(specialRequestRepository).save(request);
            verify(recipientRepository).save(recipient);
        }

        @Test
        void throwsException_whenRecipientNotFound() {
            Recipient recipient = new Recipient();
            recipient.setId(99L);

            SpecialRequest request = new SpecialRequest();
            request.setRecipient(recipient);

            when(recipientRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RecipientNotFoundException.class, () -> service.addSpecialRequest(request));
            verify(specialRequestRepository, never()).save(any());
        }
    }
}
