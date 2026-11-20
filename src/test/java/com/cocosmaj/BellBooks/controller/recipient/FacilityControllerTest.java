package com.cocosmaj.BellBooks.controller.recipient;

import com.cocosmaj.BellBooks.exception.FacilityNotFoundException;
import com.cocosmaj.BellBooks.model.enums.State;
import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.service.recipient.FacilityService;
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

@WebMvcTest(FacilityController.class)
class FacilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacilityService facilityService;

    @Nested
    class GetFacility {

        @Test
        void returns200_whenFound() throws Exception {
            Facility facility = new Facility();
            facility.setId(1L);
            facility.setName("Central Prison");

            when(facilityService.getFacilityById(1L)).thenReturn(facility);

            mockMvc.perform(get("/getFacility").param("id", "1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value("Central Prison"));
        }

        @Test
        void returns400_whenNotFound() throws Exception {
            when(facilityService.getFacilityById(99L)).thenThrow(new FacilityNotFoundException());

            mockMvc.perform(get("/getFacility").param("id", "99"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class GetFacilityByNameAndState {

        @Test
        void returns200_withResults() throws Exception {
            Facility facility = new Facility();
            facility.setId(1L);
            facility.setName("Central Prison");

            when(facilityService.getFacilityByNameAndState("Central Prison", State.NC))
                .thenReturn(List.of(facility));

            mockMvc.perform(get("/getFacilityByName")
                    .param("name", "Central Prison")
                    .param("state", "NC"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        void returns400_whenStateIsInvalid() throws Exception {
            mockMvc.perform(get("/getFacilityByName")
                    .param("name", "Central Prison")
                    .param("state", "XX"))
                .andExpect(status().is(400));
        }

        @Test
        void returns400_whenNameParamIsMissing() throws Exception {
            mockMvc.perform(get("/getFacilityByName")
                    .param("state", "NC"))
                .andExpect(status().is(400));
        }

        @Test
        void returns400_whenStateParamIsMissing() throws Exception {
            mockMvc.perform(get("/getFacilityByName")
                    .param("name", "Central Prison"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class DeleteFacility {

        @Test
        void returns200_whenDeleted() throws Exception {
            doNothing().when(facilityService).deleteFacility(1L);

            mockMvc.perform(delete("/deleteFacility").param("id", "1"))
                .andExpect(status().is(200));
        }

        @Test
        void returns400_whenNotFound() throws Exception {
            doThrow(new FacilityNotFoundException()).when(facilityService).deleteFacility(99L);

            mockMvc.perform(delete("/deleteFacility").param("id", "99"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class UpdateFacility {

        @Test
        void returns400_whenNotFound() throws Exception {
            when(facilityService.updateFacility(any())).thenThrow(new FacilityNotFoundException());

            mockMvc.perform(put("/updateFacility")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":99,\"name\":\"Unknown\",\"street\":\"123 Main\",\"state\":\"NC\",\"zip\":\"27601\"}"))
                .andExpect(status().is(400));
        }
    }
}
