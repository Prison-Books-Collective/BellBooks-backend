package com.cocosmaj.BellBooks.util;

import com.cocosmaj.BellBooks.repository.shipment.BookRepository;
import com.cocosmaj.BellBooks.repository.shipment.PackageContentRepository;
import com.cocosmaj.BellBooks.model.shipment.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class GoogleBookAPIService {

    private PackageContentRepository packageContentRepository;

    private BookRepository bookRepository;

    private HttpClient httpClient;


    public GoogleBookAPIService(PackageContentRepository packageContentRepository, BookRepository bookRepository){
        this.packageContentRepository = packageContentRepository;
        this.bookRepository = bookRepository;
        this.httpClient = HttpClient.newHttpClient();
    }

    public Book queryGoogle(String isbn) {
        Book book = new Book();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format("https://www.googleapis.com/books/v1/volumes?q=isbn:%s", isbn)))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject googleBook = new JSONObject(response.body());
            JSONArray items = googleBook.getJSONArray("items");
            for (int index = 0; index < items.length(); index++) {
                JSONObject item = items.getJSONObject(index);

                if(!item.has("volumeInfo")) continue;
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                if(!volumeInfo.has("title")) continue;
                book.setTitle(volumeInfo.getString("title"));

                if(volumeInfo.has("authors")) {
                    List<String> authors = volumeInfo.getJSONArray("authors").toList().stream()
                            .map(authorName -> (String) authorName)
                            .collect(Collectors.toList());
                    book.setAuthors(String.join("; ", authors));
                }

                if(!volumeInfo.has("industryIdentifiers")) continue;
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                for (int isbnIndex = 0; isbnIndex < industryIdentifiers.length(); isbnIndex++) {
                    if (book.getISBN10() != null && book.getISBN13() != null) break;

                    JSONObject identifier = industryIdentifiers.getJSONObject(isbnIndex);

                    if (book.getISBN10() == null && identifier.has("identifier") && identifier.has("type") && identifier.getString("type").equals("ISBN_10")) {
                        String googleIsbn = identifier.getString("identifier").replaceAll("-", "");
                        book.setISBN10(googleIsbn);
                    }
                    if (book.getISBN13() == null && identifier.has("identifier") && identifier.has("type") && identifier.getString("type").equals("ISBN_13")) {
                        String googleIsbn = identifier.getString("identifier").replaceAll("-", "");
                        book.setISBN13(googleIsbn);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(book.getTitle() != null && book.getISBN10() == null && book.getISBN13() == null) {
            if(isbn.length() == 10) {
                book.setISBN10(isbn);
            } else {
                book.setISBN13(isbn);
            }
        }

        if(book.getTitle() == null) throw new RuntimeException("Book from Google does not have a title");
        return (Book) packageContentRepository.save(book);
    }


//    public Optional<Book> queryGoogle(String isbn) throws InterruptedException {
//        Mono<String> response = webClient.get().uri("/books/v1/volumes?q=isbn:{isbn}", isbn).retrieve().bodyToMono(String.class);
//        ObjectMapper mapper = new ObjectMapper();
//        Book book = new Book();
//        Set<Creator> creators = new HashSet<>();
//        AtomicBoolean done = new AtomicBoolean(false);
//        AtomicBoolean needAuthorIntervention = new AtomicBoolean(false);
//        ArrayList<String> authorInterventions = new ArrayList<>();
//        response.subscribe(result -> {
//            try {
//                Map<String, String> map = mapper.readValue(result, HashMap.class);
//
//                for (Map.Entry entry : map.entrySet()) {
//                    if (entry.getKey().equals("items")) {
//                        ArrayList<HashMap<String, String>> itemsList = (ArrayList) entry.getValue();
//                        for (int i = 0; i < itemsList.size(); i++) {
//                            for (Map.Entry itemEntry : itemsList.get(i).entrySet()) {
//                                if (itemEntry.getKey().equals("volumeInfo")) {
//                                    HashMap<String, String> volumeInfo = (HashMap<String, String>) itemEntry.getValue();
//                                    System.out.println(volumeInfo);
//                                    for (Map.Entry volumeEntry : volumeInfo.entrySet()) {
//                                        if (volumeEntry.getKey().equals("title")) {
//                                            book.setTitle((String) volumeEntry.getValue());
//                                        } else if (volumeEntry.getKey().equals("authors")) {
//                                            ArrayList<String> authors = (ArrayList) volumeEntry.getValue();
//                                            for (String author : authors) {
//                                                String[] authorSplit = author.split(" ");
//                                                if (authorSplit.length == 2) {
//                                                    Author authorToAdd = new Author();
//                                                    authorToAdd.setFirstName(authorSplit[0]);
//                                                    authorToAdd.setLastName(authorSplit[1]);
//                                                    creatorService.setAuthorId(authorToAdd);
//                                                    creators.add(authorToAdd);
//                                                } else {
//                                                    needAuthorIntervention.set(true);
//                                                    authorInterventions.add(author);
//                                                }
//                                            }
//                                            book.setCreators(creators);
//                                        } else if (volumeEntry.getKey().equals("industryIdentifiers")) {
//                                            ArrayList<HashMap<String, String>> identifiersList = (ArrayList) volumeEntry.getValue();
//                                            for (HashMap<String, String> identifierEntry : identifiersList) {
//                                                boolean isbn10 = false;
//                                                boolean isbn13 = false;
//                                                for (Map.Entry<String, String> isbnEntry : identifierEntry.entrySet()) {
//                                                    if (isbnEntry.getKey().equals("type")) {
//                                                        if (isbnEntry.getValue().equals("ISBN_10")) {
//                                                            isbn10 = true;
//                                                        } else if (isbnEntry.getValue().equals("ISBN_13")) {
//                                                            isbn13 = true;
//                                                        }
//                                                    }
//                                                    if (isbnEntry.getKey().equals("identifier")) {
//                                                        if (isbn10) {
//                                                            book.setISBN10(isbnEntry.getValue());
//                                                        } else if (isbn13) {
//                                                            book.setISBN13(isbnEntry.getValue());
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                if (book.getTitle() != null && !needAuthorIntervention.get()) {
//                    if (book.getISBN10() == null && book.getISBN13() == null) {
//                        if (isbn.length() == 10){
//                            book.setISBN10(isbn);
//                        } else if (isbn.length() == 13) {
//                            book.setISBN13(isbn);
//                        }
//                    }
//                    packageContentRepository.save(book);
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//            done.set(true);
//        });
//        while (!done.get()){
//
//        }
//        if (needAuthorIntervention.get()){
//            for (String author : authorInterventions){
//                AuthorGroup authorGroup = new AuthorGroup();
//                authorGroup.setName(author);
//                creators.add(authorGroup);
//            }
//            book.setCreators(creators);
//            return Optional.of(book);
//        }
//        if (isbn.length()==10) {
//            return bookRepository.findByISBN10(isbn);
//        } else {
//            return bookRepository.findByISBN13(isbn);
//        }
//
//    }
}
