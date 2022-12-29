package com.cocosmaj.BellBooks.model.shipment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=Author.class, name = "author"),
        @JsonSubTypes.Type(value= Group.class, name = "group")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Creator {

    @Id
    @GeneratedValue
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
