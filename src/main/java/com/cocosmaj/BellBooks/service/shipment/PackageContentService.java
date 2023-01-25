package com.cocosmaj.BellBooks.service.shipment;

import com.cocosmaj.BellBooks.exception.AuthorInterventionNeededException;
import com.cocosmaj.BellBooks.exception.PackageContentNotFoundException;
import com.cocosmaj.BellBooks.model.shipment.*;
import com.cocosmaj.BellBooks.repository.BookRepository;
import com.cocosmaj.BellBooks.repository.CreatorRepository;
import com.cocosmaj.BellBooks.repository.PackageContentRepository;
import com.cocosmaj.BellBooks.repository.ZineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

    public Optional<Book> queryGoogle(String isbn) throws InterruptedException {
        Mono<String> response = this.webClient.get().uri("/books/v1/volumes?q=isbn:{isbn}", isbn).retrieve().bodyToMono(String.class);
        ObjectMapper mapper = new ObjectMapper();
        Book book = new Book();
        Set<Creator> creators = new HashSet<>();
        AtomicBoolean done = new AtomicBoolean(false);
        AtomicBoolean needAuthorIntervention = new AtomicBoolean(false);
        ArrayList<String> authorInterventions = new ArrayList<>();
        response.subscribe(result -> {
            try {
                Map<String, String> map = mapper.readValue(result, HashMap.class);

                for (Map.Entry entry : map.entrySet()) {
                    if (entry.getKey().equals("items")) {
                        ArrayList<HashMap<String, String>> itemsList = (ArrayList) entry.getValue();
                        for (int i = 0; i < itemsList.size(); i++) {
                            for (Map.Entry itemEntry : itemsList.get(i).entrySet()) {
                                if (itemEntry.getKey().equals("volumeInfo")) {
                                    HashMap<String, String> volumeInfo = (HashMap<String, String>) itemEntry.getValue();
                                    System.out.println(volumeInfo);
                                    for (Map.Entry volumeEntry : volumeInfo.entrySet()) {
                                        if (volumeEntry.getKey().equals("title")) {
                                            book.setTitle((String) volumeEntry.getValue());
                                        } else if (volumeEntry.getKey().equals("authors")) {
                                            ArrayList<String> authors = (ArrayList) volumeEntry.getValue();
                                            for (String author : authors) {
                                                String[] authorSplit = author.split(" ");
                                                if (authorSplit.length == 2) {
                                                    Author authorToAdd = new Author();
                                                    authorToAdd.setFirstName(authorSplit[0]);
                                                    authorToAdd.setLastName(authorSplit[1]);
                                                    setAuthorId(authorToAdd);
                                                    creators.add(authorToAdd);
                                                } else {
                                                    needAuthorIntervention.set(true);
                                                    authorInterventions.add(author);
                                                }
                                            }
                                            book.setCreators(creators);
                                        } else if (volumeEntry.getKey().equals("industryIdentifiers")) {
                                            ArrayList<HashMap<String, String>> identifiersList = (ArrayList) volumeEntry.getValue();
                                            for (HashMap<String, String> identifierEntry : identifiersList) {
                                                boolean isbn10 = false;
                                                boolean isbn13 = false;
                                                for (Map.Entry<String, String> isbnEntry : identifierEntry.entrySet()) {
                                                    if (isbnEntry.getKey().equals("type")) {
                                                        if (isbnEntry.getValue().equals("ISBN_10")) {
                                                            isbn10 = true;
                                                        } else if (isbnEntry.getValue().equals("ISBN_13")) {
                                                            isbn13 = true;
                                                        }
                                                    }
                                                    if (isbnEntry.getKey().equals("identifier")) {
                                                        if (isbn10) {
                                                            book.setISBN10(isbnEntry.getValue());
                                                        } else if (isbn13) {
                                                            book.setISBN13(isbnEntry.getValue());
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (book.getTitle() != null && !needAuthorIntervention.get()) {
                    if (book.getISBN10() == null && book.getISBN13() == null) {
                        if (isbn.length() == 10){
                            book.setISBN10(isbn);
                        } else if (isbn.length() == 13) {
                            book.setISBN13(isbn);
                        }
                    }
                    packageContentRepository.save(book);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            done.set(true);
        });
        while (!done.get()){

        }
        if (needAuthorIntervention.get()){
            for (String author : authorInterventions){
                Group group = new Group();
                group.setName(author);
                creators.add(group);
            }
            book.setCreators(creators);
            return Optional.of(book);
        }
        if (isbn.length()==10) {
            return bookRepository.findByISBN10(isbn);
        } else {
            return bookRepository.findByISBN13(isbn);
        }

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
