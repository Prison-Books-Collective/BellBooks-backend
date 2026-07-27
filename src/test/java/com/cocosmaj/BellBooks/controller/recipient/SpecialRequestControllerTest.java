package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.exception.RecipientNotFoundException;
import com.cocosmaj.BellBooks.model.recipient.SpecialRequest;
import com.cocosmaj.BellBooks.service.recipient.SpecialRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecialRequestController.class)
class SpecialRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecialRequestService specialRequestService;

    @Test
    void addSpecialRequest_returns200_whenRecipientExists() throws Exception {
        SpecialRequest saved = new SpecialRequest();
        saved.setId(1L);
        saved.setRequest("Need legal books");

        when(specialRequestService.addSpecialRequest(any())).thenReturn(saved);

        mockMvc.perform(post("/addSpecialRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"request\":\"Need legal books\",\"recipient\":{\"id\":1}}"))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.request").value("Need legal books"));
    }

    @Test
    void addSpecialRequest_returns400_whenRecipientNotFound() throws Exception {
        when(specialRequestService.addSpecialRequest(any())).thenThrow(new RecipientNotFoundException());

        mockMvc.perform(post("/addSpecialRequest")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"request\":\"Need legal books\",\"recipient\":{\"id\":99}}"))
            .andExpect(status().is(400));
    }
}
