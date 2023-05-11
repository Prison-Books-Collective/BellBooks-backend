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
  @JsonSubTypes.Type(value=ActivityBook.class, name = "activityBook"),
  @JsonSubTypes.Type(value=Book.class, name = "book"),
  @JsonSubTypes.Type(value=Comic.class, name = "comic"),
  @JsonSubTypes.Type(value=Magazine.class, name = "magazine"),
  @JsonSubTypes.Type(value=Zine.class, name = "zine")

})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PackageContent {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
}
