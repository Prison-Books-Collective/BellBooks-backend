package com.cocosmaj.BellBooks.model.shipment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name="author")
public class Author extends Creator{

    private String prefix;

    private String firstName;

    private String middleName;

    private String lastName;

    private String suffix;
}
