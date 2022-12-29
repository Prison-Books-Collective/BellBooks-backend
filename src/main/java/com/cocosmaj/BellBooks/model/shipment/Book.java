package com.cocosmaj.BellBooks.model.shipment;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name="book")
public class Book extends PackageContent {

    @Column(unique = true)
    private String ISBN10;

    @Column(unique = true)
    private String ISBN13;

    @Enumerated(value= EnumType.STRING)
    private BookGenre genre;

    @ManyToMany
    private Set<Creator> creators;

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

    public Set<Creator> getCreators() {
        return creators;
    }

    public void setCreators(Set<Creator> creators) {
        this.creators = creators;
    }
}
