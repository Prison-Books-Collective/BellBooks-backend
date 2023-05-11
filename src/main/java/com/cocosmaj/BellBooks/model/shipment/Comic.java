package com.cocosmaj.BellBooks.model.shipment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity(name="comic")
public class Comic extends PackageContent {

    private String issueNumber;

    private LocalDate date;
}
