package com.cocosmaj.BellBooks.model.shipment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=Author.class, name = "author"),
        @JsonSubTypes.Type(value=AuthorGroup.class, name = "authorGroup")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Creator {

    @Id
    @GeneratedValue
    private long id;
}
