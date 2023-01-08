package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.*;
import com.cocosmaj.BellBooks.repository.BookRepository;
import com.cocosmaj.BellBooks.repository.CreatorRepository;
import com.cocosmaj.BellBooks.repository.PackageContentRepository;
import com.cocosmaj.BellBooks.repository.ZineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpHeaders;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PackageContentService {


    private PackageContentRepository<PackageContent> packageContentRepository;

    private ZineRepository zineRepository;

    private BookRepository bookRepository;
    private CreatorRepository creatorRepository;

    public WebClient webClient;

    public PackageContentService( PackageContentRepository packageContentRepository,  CreatorRepository creatorRepository, ZineRepository zineRepository, BookRepository bookRepository, WebClient.Builder webClientBuilder){
        this.packageContentRepository = packageContentRepository;
        this.creatorRepository = creatorRepository;
        this.zineRepository = zineRepository;
        this.bookRepository = bookRepository;
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com").build();
    }

    public PackageContent addContent(PackageContent packageContent) {
        if (packageContent.getClass()== Book.class){
            Book book = (Book) packageContent;
            Set<Creator> creators = book.getCreators();
            if (creators != null && creators.size() > 0){
                for (Creator creator : creators){
                    if (creator.getClass() == Author.class){
                        setAuthorId((Author) creator);
                    } else {
                        setGroupId((Group) creator);
                    }
                }
            }
        }
        return this.packageContentRepository.save(packageContent);
    }

    private void setGroupId(Group creator) {
        Group group = creator;
        Optional<Group> optionalGroup = creatorRepository.findByName(group.getName());
        if (!optionalGroup.isPresent()){
            Group savedGroup = creatorRepository.save(group);
            group.setId(savedGroup.getId());
        } else {
            group.setId(optionalGroup.get().getId());
        }
    }

    private void setAuthorId(Author creator) {
        Author author = creator;
        Optional<Author> optionalAuthor = creatorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        if (!optionalAuthor.isPresent()){
            Author savedAuthor = creatorRepository.save(author);
            author.setId(savedAuthor.getId());
        } else {
            author.setId(optionalAuthor.get().getId());
        }
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

    public void queryGoogle(String isbn) {
        Mono<String> response = this.webClient.get().uri("/books/v1/volumes?q=isbn:{isbn}", isbn).retrieve().bodyToMono(String.class);
        response.subscribe(result -> System.out.println(result));
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
}
