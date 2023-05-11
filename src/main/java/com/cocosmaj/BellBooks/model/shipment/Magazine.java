package com.cocosmaj.BellBooks.model.shipment;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity(name="magazine")
public class Magazine extends PackageContent {

    private String volume;

    private String issue;

    private LocalDate date;

    @Enumerated(value= EnumType.STRING)
    private MagazineGenre genre;
}
