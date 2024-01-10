package com.cocosmaj.BellBooks.model.shipment;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity(name = "book")
@DiscriminatorValue("book")
public class Book extends PackageContent {

    @Column(unique = true)
    private String ISBN10;

    @Column(unique = true)
    private String ISBN13;

    private String authors;
}
