package com.cocosmaj.BellBooks.repository.shipment;

import com.cocosmaj.BellBooks.model.shipment.Book;
import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface BookRepository extends PackageContentRepository<Book> {

    Optional<Book> findByISBN13(String isbn13);

    Optional<Book> findByISBN10(String isbn10);

    List<PackageContent> findAllByTitleContaining(String title);

    List<PackageContent> findAllByTitleContainingAndAuthorsContaining(String title, String author);
    List<Book> findByISBN10IsNullAndISBN13IsNull();

}
