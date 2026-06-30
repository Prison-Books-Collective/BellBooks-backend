package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Book;
import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import com.cocosmaj.BellBooks.model.shipment.Zine;
import com.cocosmaj.BellBooks.repository.shipment.BookRepository;
import com.cocosmaj.BellBooks.repository.shipment.PackageContentRepository;
import com.cocosmaj.BellBooks.repository.shipment.ZineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageContentServiceTest {

    @Mock
    private PackageContentRepository<PackageContent> packageContentRepository;

    @Mock
    private ZineRepository zineRepository;

    @Mock
    private BookRepository bookRepository;

    private PackageContentService service;

    @BeforeEach
    void setUp() {
        service = new PackageContentService(packageContentRepository, zineRepository, bookRepository);
    }

    @Nested
    class AddContent {

        @Test
        void returnsExistingBook_whenIsbn13AlreadyExists() {
            Book existing = new Book();
            existing.setId(1L);
            existing.setTitle("Existing Book");
            existing.setISBN13("9781234567890");

            Book incoming = new Book();
            incoming.setTitle("Existing Book");
            incoming.setISBN13("9781234567890");

            when(bookRepository.findByISBN13("9781234567890")).thenReturn(Optional.of(existing));

            PackageContent result = service.addContent(incoming);

            assertSame(existing, result);
            verify(packageContentRepository, never()).save(any());
        }

        @Test
        void returnsExistingBook_whenIsbn10AlreadyExists() {
            Book existing = new Book();
            existing.setId(2L);
            existing.setTitle("Existing Book");
            existing.setISBN10("1234567890");

            Book incoming = new Book();
            incoming.setTitle("Existing Book");
            incoming.setISBN10("1234567890");

            when(bookRepository.findByISBN10("1234567890")).thenReturn(Optional.of(existing));

            PackageContent result = service.addContent(incoming);

            assertSame(existing, result);
            verify(packageContentRepository, never()).save(any());
        }

        @Test
        void checksIsbn13BeforeIsbn10() {
            Book existing = new Book();
            existing.setId(1L);
            existing.setISBN13("9781234567890");
            existing.setISBN10("1234567890");

            Book incoming = new Book();
            incoming.setISBN13("9781234567890");
            incoming.setISBN10("1234567890");
            incoming.setTitle("Some Book");

            when(bookRepository.findByISBN13("9781234567890")).thenReturn(Optional.of(existing));

            service.addContent(incoming);

            verify(bookRepository).findByISBN13("9781234567890");
            verify(bookRepository, never()).findByISBN10(any());
        }

        @Test
        void savesNewBook_whenNoIsbnMatch() {
            Book incoming = new Book();
            incoming.setTitle("New Book");
            incoming.setISBN13("9789999999999");
            incoming.setISBN10("9999999999");

            when(bookRepository.findByISBN13("9789999999999")).thenReturn(Optional.empty());
            when(bookRepository.findByISBN10("9999999999")).thenReturn(Optional.empty());
            when(packageContentRepository.save(incoming)).thenReturn(incoming);

            PackageContent result = service.addContent(incoming);

            assertSame(incoming, result);
            verify(packageContentRepository).save(incoming);
        }

        @Test
        void savesBookDirectly_whenNoIsbnsProvided() {
            Book incoming = new Book();
            incoming.setTitle("Book Without ISBNs");

            when(packageContentRepository.save(incoming)).thenReturn(incoming);

            PackageContent result = service.addContent(incoming);

            assertSame(incoming, result);
            verify(bookRepository, never()).findByISBN13(any());
            verify(bookRepository, never()).findByISBN10(any());
            verify(packageContentRepository).save(incoming);
        }

        @Test
        void savesDuplicateBook_whenBothLackIsbns() {
            Book first = new Book();
            first.setTitle("Same Title");

            Book second = new Book();
            second.setTitle("Same Title");

            when(packageContentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            service.addContent(first);
            service.addContent(second);

            verify(packageContentRepository, times(2)).save(any());
            verify(packageContentRepository).save(first);
            verify(packageContentRepository).save(second);
        }

        @Test
        void alwaysSavesZine_withoutIsbnDeduplication() {
            Zine zine = new Zine();
            zine.setTitle("Test Zine");
            zine.setCode("Z001");

            when(packageContentRepository.save(zine)).thenReturn(zine);

            PackageContent result = service.addContent(zine);

            assertSame(zine, result);
            verify(bookRepository, never()).findByISBN13(any());
            verify(bookRepository, never()).findByISBN10(any());
            verify(packageContentRepository).save(zine);
        }

        @Test
        void savesDuplicateZine_whenTitleAndCodeAlreadyExist() {
            Zine first = new Zine();
            first.setTitle("Prison Abolition Weekly");
            first.setCode("PAW-01");

            Zine second = new Zine();
            second.setTitle("Prison Abolition Weekly");
            second.setCode("PAW-01");

            when(packageContentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            service.addContent(first);
            service.addContent(second);

            verify(packageContentRepository).save(first);
            verify(packageContentRepository).save(second);
        }
    }

    @Nested
    class GetContent {

        @Test
        void returnsContent_whenFound() throws PackageContentNotFoundException {
            Book book = new Book();
            book.setId(1L);
            book.setTitle("Found Book");

            when(packageContentRepository.findById(1L)).thenReturn(Optional.of(book));

            PackageContent result = service.getContent(1L);

            assertSame(book, result);
        }

        @Test
        void throwsException_whenNotFound() {
            when(packageContentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(PackageContentNotFoundException.class, () -> service.getContent(99L));
        }
    }

    @Nested
    class UpdateContent {

        @Test
        void savesContent_whenItExists() throws PackageContentNotFoundException {
            Book book = new Book();
            book.setId(1L);
            book.setTitle("Updated Title");

            when(packageContentRepository.findById(1L)).thenReturn(Optional.of(book));
            when(packageContentRepository.save(book)).thenReturn(book);

            PackageContent result = service.updateContent(book);

            assertSame(book, result);
            verify(packageContentRepository).save(book);
        }

        @Test
        void throwsException_whenContentDoesNotExist() {
            Book book = new Book();
            book.setId(99L);

            when(packageContentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(PackageContentNotFoundException.class, () -> service.updateContent(book));
        }
    }

    @Nested
    class DeleteContent {

        @Test
        void deletesContent_whenItExists() throws PackageContentNotFoundException {
            Book book = new Book();
            book.setId(1L);

            when(packageContentRepository.findById(1L)).thenReturn(Optional.of(book));

            service.deleteContent(1L);

            verify(packageContentRepository).deleteById(1L);
        }

        @Test
        void throwsException_whenContentDoesNotExist() {
            when(packageContentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(PackageContentNotFoundException.class, () -> service.deleteContent(99L));
        }
    }

    @Nested
    class GetContentByTitleAndAuthor {

        @Test
        void searchesByTitleOnly_whenAuthorIsNull() {
            List<PackageContent> expected = List.of();
            when(bookRepository.findAllByTitleContaining("Java")).thenReturn(expected);

            List<PackageContent> result = service.getContentByTitleAndAuthor("Java", null);

            assertSame(expected, result);
            verify(bookRepository).findAllByTitleContaining("Java");
            verify(bookRepository, never()).findAllByTitleContainingAndAuthorsContaining(any(), any());
        }

        @Test
        void searchesByTitleOnly_whenAuthorIsEmpty() {
            List<PackageContent> expected = List.of();
            when(bookRepository.findAllByTitleContaining("Java")).thenReturn(expected);

            List<PackageContent> result = service.getContentByTitleAndAuthor("Java", "");

            assertSame(expected, result);
            verify(bookRepository).findAllByTitleContaining("Java");
            verify(bookRepository, never()).findAllByTitleContainingAndAuthorsContaining(any(), any());
        }

        @Test
        void searchesByTitleAndAuthor_whenAuthorIsProvided() {
            List<PackageContent> expected = List.of();
            when(bookRepository.findAllByTitleContainingAndAuthorsContaining("Java", "Bloch"))
                .thenReturn(expected);

            List<PackageContent> result = service.getContentByTitleAndAuthor("Java", "Bloch");

            assertSame(expected, result);
            verify(bookRepository).findAllByTitleContainingAndAuthorsContaining("Java", "Bloch");
        }
    }
}
