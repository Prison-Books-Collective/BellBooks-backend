package com.cocosmaj.BellBooks.service.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.repository.recipient.RecipientRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipientServiceTest {

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private RecipientService service;

    @Nested
    class UpdateRecipient {

        @Test
        void throwsException_whenRecipientNotFound() {
            Recipient incoming = new Recipient();
            incoming.setId(99L);

            when(recipientRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RecipientNotFoundException.class, () -> service.updateRecipient(incoming));
            verify(recipientRepository, never()).save(any());
        }

        @Test
        void updatesAssignedId_whenChanged() throws RecipientNotFoundException {
            Recipient existing = new Recipient();
            existing.setId(1L);
            existing.setAssignedId("OLD_ID");
            existing.setFirstName("John");
            existing.setLastName("Doe");

            Recipient incoming = new Recipient();
            incoming.setId(1L);
            incoming.setAssignedId("NEW_ID");
            incoming.setFirstName("John");
            incoming.setLastName("Doe");

            when(recipientRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(recipientRepository.save(existing)).thenReturn(existing);

            Recipient result = service.updateRecipient(incoming);

            assertEquals("NEW_ID", result.getAssignedId());
            verify(recipientRepository).save(existing);
        }

        @Test
        void updatesFirstName_whenChanged() throws RecipientNotFoundException {
            Recipient existing = new Recipient();
            existing.setId(1L);
            existing.setAssignedId("ID001");
            existing.setFirstName("John");
            existing.setLastName("Doe");

            Recipient incoming = new Recipient();
            incoming.setId(1L);
            incoming.setAssignedId("ID001");
            incoming.setFirstName("Jonathan");
            incoming.setLastName("Doe");

            when(recipientRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(recipientRepository.save(existing)).thenReturn(existing);

            Recipient result = service.updateRecipient(incoming);

            assertEquals("Jonathan", result.getFirstName());
        }

        @Test
        void updatesLastName_whenChanged() throws RecipientNotFoundException {
            Recipient existing = new Recipient();
            existing.setId(1L);
            existing.setAssignedId("ID001");
            existing.setFirstName("John");
            existing.setLastName("Doe");

            Recipient incoming = new Recipient();
            incoming.setId(1L);
            incoming.setAssignedId("ID001");
            incoming.setFirstName("John");
            incoming.setLastName("Smith");

            when(recipientRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(recipientRepository.save(existing)).thenReturn(existing);

            Recipient result = service.updateRecipient(incoming);

            assertEquals("Smith", result.getLastName());
        }

        @Test
        void doesNotModifyFields_whenNothingChanged() throws RecipientNotFoundException {
            Recipient existing = new Recipient();
            existing.setId(1L);
            existing.setAssignedId("ID001");
            existing.setFirstName("John");
            existing.setLastName("Doe");

            Recipient incoming = new Recipient();
            incoming.setId(1L);
            incoming.setAssignedId("ID001");
            incoming.setFirstName("John");
            incoming.setLastName("Doe");

            when(recipientRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(recipientRepository.save(existing)).thenReturn(existing);

            Recipient result = service.updateRecipient(incoming);

            assertEquals("ID001", result.getAssignedId());
            assertEquals("John", result.getFirstName());
            assertEquals("Doe", result.getLastName());
        }
    }

    @Nested
    class GetRecipientById {

        @Test
        void returnsRecipient_whenFound() throws RecipientNotFoundException {
            Recipient recipient = new Recipient();
            recipient.setId(1L);

            when(recipientRepository.findById(1L)).thenReturn(Optional.of(recipient));

            Recipient result = service.getRecipientById(1L);

            assertSame(recipient, result);
        }

        @Test
        void throwsException_whenNotFound() {
            when(recipientRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RecipientNotFoundException.class, () -> service.getRecipientById(99L));
        }
    }

    @Nested
    class GetRecipientByAssignedId {

        @Test
        void returnsRecipient_whenFound() throws RecipientNotFoundException {
            Recipient recipient = new Recipient();
            recipient.setId(1L);
            recipient.setAssignedId("ABC1234");

            when(recipientRepository.findByAssignedId("ABC1234")).thenReturn(Optional.of(recipient));

            Recipient result = service.getRecipientByAssignedId("ABC1234");

            assertSame(recipient, result);
        }

        @Test
        void throwsException_whenNotFound() {
            when(recipientRepository.findByAssignedId("UNKNOWN")).thenReturn(Optional.empty());

            assertThrows(RecipientNotFoundException.class,
                () -> service.getRecipientByAssignedId("UNKNOWN"));
        }
    }

    @Nested
    class DeleteRecipient {

        @Test
        void deletesRecipient_whenFound() throws RecipientNotFoundException {
            Recipient recipient = new Recipient();
            recipient.setId(1L);

            when(recipientRepository.findById(1L)).thenReturn(Optional.of(recipient));

            service.deleteRecipient(1L);

            verify(recipientRepository).deleteById(1L);
        }

        @Test
        void throwsException_whenNotFound() {
            when(recipientRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RecipientNotFoundException.class, () -> service.deleteRecipient(99L));
            verify(recipientRepository, never()).deleteById(any());
        }
    }

    @Nested
    class GetRecipientLocation {

        @Test
        void returnsError_whenIdLengthIsNot7() throws Exception {
            assertEquals("Id.length != 7", service.getRecipientLocation("123"));
        }

        @Test
        void returnsError_whenIdIsTooLong() throws Exception {
            assertEquals("Id.length != 7", service.getRecipientLocation("12345678"));
        }

        @Test
        void returnsError_whenIdIsEmpty() throws Exception {
            assertEquals("Id.length != 7", service.getRecipientLocation(""));
        }
    }

    @Nested
    class GetRecipients {

        @Test
        void delegatesToRepository() {
            List<Recipient> expected = List.of(new Recipient());
            when(recipientRepository.findAllByFirstNameContainingAndLastNameContaining("John", "Doe"))
                .thenReturn(expected);

            List<Recipient> result = service.getRecipients("John", "Doe");

            assertSame(expected, result);
        }
    }
}
