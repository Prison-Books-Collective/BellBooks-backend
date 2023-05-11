package com.cocosmaj.BellBooks.model.shipment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity(name="authorGroup")
public class AuthorGroup extends Creator{

    private String name;
}
