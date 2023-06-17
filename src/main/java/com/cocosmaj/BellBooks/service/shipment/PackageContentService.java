package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.*;
import com.cocosmaj.BellBooks.repository.shipment.BookRepository;
import com.cocosmaj.BellBooks.repository.shipment.PackageContentRepository;
import com.cocosmaj.BellBooks.repository.shipment.ZineRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PackageContentService {


    private PackageContentRepository<PackageContent> packageContentRepository;

    private ZineRepository zineRepository;

    private BookRepository bookRepository;




    public PackageContentService(PackageContentRepository packageContentRepository, ZineRepository zineRepository, BookRepository bookRepository){
        this.packageContentRepository = packageContentRepository;
        this.zineRepository = zineRepository;
        this.bookRepository = bookRepository;
    }

    public PackageContent addContent(PackageContent packageContent) {
        return this.packageContentRepository.save(packageContent);
    }



    public PackageContent getContent(Long id) throws PackageContentNotFoundException {
        Optional<PackageContent> byId = packageContentRepository.findById(id);
        if (byId.isPresent()){
            return byId.get();
        } else {
            throw new PackageContentNotFoundException();
        }

    }

    public PackageContent updateContent(PackageContent packageContent) throws PackageContentNotFoundException {
        getContent(packageContent.getId());
        return packageContentRepository.save(packageContent);
    }

    public void deleteContent(Long id) throws PackageContentNotFoundException {
        getContent(id);
        packageContentRepository.deleteById(id);
    }

    public List<PackageContent> getAllContent() {
        return (List) packageContentRepository.findAll();
    }

    public Optional<Book> getBookByIsbn13(String isbn13) {
        return bookRepository.findByISBN13(isbn13);
    }

    public Optional<Book> getBookByIsbn10(String isbn10) {
        return bookRepository.findByISBN10(isbn10);
    }

    public List<PackageContent> getContentByTitle(String title) {
        return bookRepository.findAllByTitleContaining(title);
    }

    public List<Book> getBooksWithNoIsbn() {
        return bookRepository.findByISBN10IsNullAndISBN13IsNull();
    }

    public Optional<Zine> getZineByCode(String code) {
        return zineRepository.findByCode(code);
    }

    public List<Zine> getAllZines() {
        return (List) zineRepository.findAll();
    }

    public List<PackageContent> getContentByTitleAndAuthor(String title, String author) {
        return bookRepository.findAllByTitleContainingAndAuthorsContaining(title, author);
    }
}
