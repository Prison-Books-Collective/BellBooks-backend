package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.cocosmaj.BellBooks.service.recipient.RecipientService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipientController.class)
class RecipientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipientService recipientService;

    @Nested
    class GetRecipient {

        @Test
        void returns200_whenFound() throws Exception {
            Recipient recipient = new Recipient();
            recipient.setId(1L);
            recipient.setFirstName("John");
            recipient.setLastName("Doe");

            when(recipientService.getRecipientById(1L)).thenReturn(recipient);

            mockMvc.perform(get("/getRecipient").param("id", "1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.firstName").value("John"));
        }

        @Test
        void returns400_whenNotFound() throws Exception {
            when(recipientService.getRecipientById(99L)).thenThrow(new RecipientNotFoundException());

            mockMvc.perform(get("/getRecipient").param("id", "99"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class GetRecipientByAssignedId {

        @Test
        void returns200_whenFound() throws Exception {
            Recipient recipient = new Recipient();
            recipient.setId(1L);
            recipient.setAssignedId("ABC1234");

            when(recipientService.getRecipientByAssignedId("ABC1234")).thenReturn(recipient);

            mockMvc.perform(get("/getRecipientByAssignedId").param("assignedId", "ABC1234"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.assignedId").value("ABC1234"));
        }

        @Test
        void returns404_whenNotFound_noteBehaviorDiffersFromGetRecipient() throws Exception {
            // NOTE: this returns 404, while /getRecipient returns 400 for the same situation.
            // This inconsistency is documented by this test.
            when(recipientService.getRecipientByAssignedId("UNKNOWN"))
                .thenThrow(new RecipientNotFoundException());

            mockMvc.perform(get("/getRecipientByAssignedId").param("assignedId", "UNKNOWN"))
                .andExpect(status().is(404));
        }
    }

    @Nested
    class UpdateRecipient {

        @Test
        void returns200_withUpdatedRecipient() throws Exception {
            Recipient updated = new Recipient();
            updated.setId(1L);
            updated.setFirstName("Jonathan");
            updated.setLastName("Doe");
            updated.setAssignedId("ID001");

            when(recipientService.updateRecipient(any())).thenReturn(updated);

            mockMvc.perform(put("/updateRecipient")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"firstName\":\"Jonathan\",\"lastName\":\"Doe\",\"assignedId\":\"ID001\"}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.firstName").value("Jonathan"));
        }

        @Test
        void returns400_whenRecipientNotFound() throws Exception {
            when(recipientService.updateRecipient(any())).thenThrow(new RecipientNotFoundException());

            mockMvc.perform(put("/updateRecipient")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":99,\"firstName\":\"John\",\"lastName\":\"Doe\",\"assignedId\":\"ID001\"}"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class DeleteRecipient {

        @Test
        void returns200_whenDeleted() throws Exception {
            doNothing().when(recipientService).deleteRecipient(1L);

            mockMvc.perform(delete("/deleteRecipient").param("id", "1"))
                .andExpect(status().is(200));
        }

        @Test
        void returns400_whenNotFound() throws Exception {
            doThrow(new RecipientNotFoundException()).when(recipientService).deleteRecipient(99L);

            mockMvc.perform(delete("/deleteRecipient").param("id", "99"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class GetRecipientLocation {

        @Test
        void returns200_withLocationString() throws Exception {
            when(recipientService.getRecipientLocation("1234567")).thenReturn("Central Prison");

            mockMvc.perform(get("/getRecipientLocation").param("id", "1234567"))
                .andExpect(status().is(200))
                .andExpect(content().string("Central Prison"));
        }

        @Test
        void returns200_withErrorString_whenIdTooShort() throws Exception {
            when(recipientService.getRecipientLocation("123")).thenReturn("Id.length != 7");

            mockMvc.perform(get("/getRecipientLocation").param("id", "123"))
                .andExpect(status().is(200))
                .andExpect(content().string("Id.length != 7"));
        }
    }
}
