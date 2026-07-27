package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Book;
import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import com.cocosmaj.BellBooks.model.shipment.Zine;
import com.cocosmaj.BellBooks.service.shipment.PackageContentService;
import com.cocosmaj.BellBooks.util.GoogleBookAPIService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PackageContentController.class)
class PackageContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PackageContentService packageContentService;

    @MockBean
    private GoogleBookAPIService googleBookAPIService;

    @Nested
    class GetBookByISBN {

        @Test
        void returns400_whenIsbnLengthIsInvalid() throws Exception {
            mockMvc.perform(get("/getBookByISBN").param("isbn", "12345"))
                .andExpect(status().is(400));
        }

        @Test
        void returns200_whenIsbn10FoundInDatabase() throws Exception {
            Book book = new Book();
            book.setId(1L);
            book.setTitle("Found Book");
            book.setISBN10("1234567890");

            when(packageContentService.getBookByIsbn10("1234567890")).thenReturn(Optional.of(book));

            mockMvc.perform(get("/getBookByISBN").param("isbn", "1234567890"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.title").value("Found Book"));
        }

        @Test
        void returns200_whenIsbn13FoundInDatabase() throws Exception {
            Book book = new Book();
            book.setId(1L);
            book.setTitle("Found Book");
            book.setISBN13("9781234567890");

            when(packageContentService.getBookByIsbn13("9781234567890")).thenReturn(Optional.of(book));

            mockMvc.perform(get("/getBookByISBN").param("isbn", "9781234567890"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.title").value("Found Book"));
        }

        @Test
        void returns417_whenIsbn10NotInDbAndGoogleReturnsBookWithoutId() throws Exception {
            Book googleBook = new Book();
            googleBook.setTitle("Google Book");

            when(packageContentService.getBookByIsbn10("1234567890")).thenReturn(Optional.empty());
            when(googleBookAPIService.queryGoogle("1234567890")).thenReturn(googleBook);

            mockMvc.perform(get("/getBookByISBN").param("isbn", "1234567890"))
                .andExpect(status().is(417))
                .andExpect(jsonPath("$.title").value("Google Book"));
        }

        @Test
        void returns417_whenIsbn13NotInDbAndGoogleReturnsBookWithoutId() throws Exception {
            Book googleBook = new Book();
            googleBook.setTitle("Google Book");

            when(packageContentService.getBookByIsbn13("9781234567890")).thenReturn(Optional.empty());
            when(googleBookAPIService.queryGoogle("9781234567890")).thenReturn(googleBook);

            mockMvc.perform(get("/getBookByISBN").param("isbn", "9781234567890"))
                .andExpect(status().is(417))
                .andExpect(jsonPath("$.title").value("Google Book"));
        }
    }

    @Nested
    class GetContent {

        @Test
        void returns200_whenContentFound() throws Exception {
            Book book = new Book();
            book.setId(1L);
            book.setTitle("Test Book");

            when(packageContentService.getContent(1L)).thenReturn(book);

            mockMvc.perform(get("/getContent").param("id", "1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.title").value("Test Book"));
        }

        @Test
        void returns400_whenContentNotFound() throws Exception {
            when(packageContentService.getContent(99L)).thenThrow(new PackageContentNotFoundException());

            mockMvc.perform(get("/getContent").param("id", "99"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class GetZineByCode {

        @Test
        void returns200_whenZineFound() throws Exception {
            Zine zine = new Zine();
            zine.setId(1L);
            zine.setTitle("Test Zine");
            zine.setCode("Z001");

            when(packageContentService.getZineByCode("Z001")).thenReturn(Optional.of(zine));

            mockMvc.perform(get("/getZineByCode").param("code", "Z001"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.title").value("Test Zine"));
        }

        @Test
        void returns400_whenZineNotFound() throws Exception {
            when(packageContentService.getZineByCode("UNKNOWN")).thenReturn(Optional.empty());

            mockMvc.perform(get("/getZineByCode").param("code", "UNKNOWN"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class SearchBooks {

        @Test
        void mergesResultsFromDatabaseAndGoogle() throws Exception {
            Book dbBook = new Book();
            dbBook.setId(1L);
            dbBook.setTitle("DB Book");
            dbBook.setISBN10("1111111111");

            Book googleBook = new Book();
            googleBook.setTitle("Google Book");
            googleBook.setISBN10("2222222222");

            when(packageContentService.getContentByTitleAndAuthor("Java", null))
                .thenReturn(List.of(dbBook));
            when(googleBookAPIService.queryGoogle("Java", null))
                .thenReturn(List.of(googleBook));

            mockMvc.perform(get("/searchBooks").param("title", "Java"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("DB Book"))
                .andExpect(jsonPath("$[1].title").value("Google Book"));
        }

        @Test
        void filtersOutNonBookContentFromDatabase() throws Exception {
            Zine zine = new Zine();
            zine.setId(1L);
            zine.setTitle("A Zine About Java");
            zine.setCode("Z001");

            when(packageContentService.getContentByTitleAndAuthor("Java", null))
                .thenReturn(List.of(zine));
            when(googleBookAPIService.queryGoogle("Java", null))
                .thenReturn(List.of());

            mockMvc.perform(get("/searchBooks").param("title", "Java"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    class UpdateContent {

        @Test
        void returns400_whenContentNotFound() throws Exception {
            when(packageContentService.updateContent(any())).thenThrow(new PackageContentNotFoundException());

            mockMvc.perform(put("/updateContent")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"type\":\"book\",\"id\":99,\"title\":\"Nonexistent\"}"))
                .andExpect(status().is(400));
        }
    }

    @Nested
    class DeleteContent {

        @Test
        void returns200_whenDeleted() throws Exception {
            doNothing().when(packageContentService).deleteContent(1L);

            mockMvc.perform(delete("/deleteContent").param("id", "1"))
                .andExpect(status().is(200));
        }

        @Test
        void returns400_whenContentNotFound() throws Exception {
            doThrow(new PackageContentNotFoundException()).when(packageContentService).deleteContent(99L);

            mockMvc.perform(delete("/deleteContent").param("id", "99"))
                .andExpect(status().is(400));
        }
    }
}
