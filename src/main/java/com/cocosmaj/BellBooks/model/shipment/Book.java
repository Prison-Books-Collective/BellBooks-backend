package com.cocosmaj.BellBooks.model.shipment;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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
}
