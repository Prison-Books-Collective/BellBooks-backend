package com.cocosmaj.BellBooks.controller.shipment;

import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.Book;
import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import com.cocosmaj.BellBooks.model.shipment.Zine;
import com.cocosmaj.BellBooks.service.shipment.PackageContentService;
import com.cocosmaj.BellBooks.util.GoogleBookAPIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController
public class PackageContentController {


    private final PackageContentService packageContentService;
    private final GoogleBookAPIService googleBookAPIService;

    public PackageContentController(PackageContentService packageContentService, GoogleBookAPIService googleBookAPIService) {
        this.packageContentService = packageContentService;
        this.googleBookAPIService = googleBookAPIService;
    }

    //get content by ID
    @GetMapping("/getContent")
    public ResponseEntity<PackageContent> getContent(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(packageContentService.getContent(id));
        } catch (PackageContentNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBookByISBN")
    public ResponseEntity<Book> getBookByISBN(@RequestParam String isbn) {
        if (isbn.length() == 10) {
            Optional<Book> bookByIsbn10 = getBookByIsbn10(isbn);
            if (bookByIsbn10.isPresent()) {
                if (bookByIsbn10.get().getId() == null) {
                    return new ResponseEntity<>(bookByIsbn10.get(), HttpStatus.EXPECTATION_FAILED);
                }
                return ResponseEntity.ok(bookByIsbn10.get());
            }
        } else if (isbn.length() == 13) {
            Optional<Book> bookByIsbn13 = getBookByIsbn13(isbn);
            if (bookByIsbn13.isPresent()) {
                if (bookByIsbn13.get().getId() == null) {
                    return new ResponseEntity<>(bookByIsbn13.get(), HttpStatus.EXPECTATION_FAILED);
                }
                return ResponseEntity.ok(bookByIsbn13.get());
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

    public Book queryGoogle(String isbn) {
        return googleBookAPIService.queryGoogle(isbn);
    }

    public Optional<Book> getBookByIsbn10(String isbn10) {
        Optional<Book> optional = packageContentService.getBookByIsbn10(isbn10);
        if (optional.isPresent()) {
            return optional;
        } else {
            return Optional.of(queryGoogle(isbn10));
        }
    }

    public Optional<Book> getBookByIsbn13(String isbn13) {
        Optional<Book> optional = packageContentService.getBookByIsbn13(isbn13);
        if (optional.isPresent()) {
            return optional;
        } else {
            return Optional.of(queryGoogle(isbn13));
        }
    }

    @GetMapping("/getContentByTitle")
    public ResponseEntity<List<PackageContent>> getContentByTitle(@RequestParam String title) {
        return ResponseEntity.ok(packageContentService.getContentByTitle(title));
    }

    @GetMapping("/content")
    public ResponseEntity<List<PackageContent>> getContentByTitleAndAuthor(@RequestParam String title, @RequestParam @Nullable String author) {
        return ResponseEntity.ok(packageContentService.getContentByTitleAndAuthor(title, author));
    }

    @GetMapping("/searchBooks")
    public ResponseEntity<List<Book>> searchBooksByTitleAndAuthor(@RequestParam String title, @RequestParam @Nullable String author) {
        Stream<Book> booksInDatabase = packageContentService.getContentByTitleAndAuthor(title, author).stream()
            .filter(book -> book.getClass() == Book.class)
            .map(book -> (Book) book);
        Stream<Book> booksFromGoogle = googleBookAPIService.queryGoogle(title, author).stream();

        List<Book> searchResults = Stream.concat(booksInDatabase, booksFromGoogle).collect(Collectors.toList());
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/queryGoogle")
    public ResponseEntity<List<Book>> queryGoogleByTitleAndAuthor(@RequestParam String title, @RequestParam @Nullable String author) {
        return ResponseEntity.ok(googleBookAPIService.queryGoogle(title, author));
    }

    @GetMapping("/getZineByCode")
    public ResponseEntity<Zine> getZineByCode(@RequestParam String code) {
        Optional<Zine> optional = packageContentService.getZineByCode(code);
        return optional.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/getAllZines")
    public ResponseEntity<List<Zine>> getAllZines() {
        return ResponseEntity.ok(packageContentService.getAllZines());
    }

    @GetMapping("/getNoIsbnBooks")
    public ResponseEntity<List<Book>> getBooksWithNoIsbn() {
        return ResponseEntity.ok(packageContentService.getBooksWithNoIsbn());
    }

    @GetMapping("/getAllContent")
    public ResponseEntity<List<PackageContent>> getAllContent() {
        return ResponseEntity.ok(packageContentService.getAllContent());
    }

    //get content by type?

    @PutMapping("/updateContent")
    public ResponseEntity<PackageContent> updateContent(@RequestBody PackageContent packageContent) {
        try {
            return ResponseEntity.ok(packageContentService.updateContent(packageContent));
        } catch (PackageContentNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteContent")
    public ResponseEntity<Void> deleteContent(@RequestParam Long id) {
        try {
            packageContentService.deleteContent(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PackageContentNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addContent")
    public ResponseEntity<PackageContent> addContent(@RequestBody PackageContent packageContent) {
        return ResponseEntity.ok(this.packageContentService.addContent(packageContent));
    }
}
