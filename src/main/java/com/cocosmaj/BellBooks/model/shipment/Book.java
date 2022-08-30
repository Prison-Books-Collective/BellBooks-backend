package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.*;
import java.util.List;

@Entity(name="book")
public class Book extends PackageContent {

    private String ISBN10;

    private String ISBN13;

    @Enumerated(value= EnumType.STRING)
    private BookGenre genre;

    @ManyToMany
    private List<Author> authors;

    public String getISBN10() {
        return ISBN10;
    }

    public void setISBN10(String ISBN10) {
        this.ISBN10 = ISBN10;
    }

    public String getISBN13() {
        return ISBN13;
    }

    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> author) {
        this.authors = author;
    }
}
