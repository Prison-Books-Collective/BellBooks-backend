package com.cocosmaj.BellBooks.repository;

import com.cocosmaj.BellBooks.model.shipment.Book;
import com.cocosmaj.BellBooks.model.shipment.PackageContent;
import com.cocosmaj.BellBooks.model.shipment.Zine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageContentRepository extends CrudRepository<PackageContent, Long> {

    Optional<Book> findByISBN13(String isbn13);

    Optional<Book> findByISBN10(String isbn10);

    List<PackageContent> findAllByTitleContaining(String title);

    List<Book> findByISBN10IsNullAndISBN13IsNull();

    Optional<Zine> findByCode(String code);
}
