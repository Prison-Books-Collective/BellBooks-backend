package com.cocosmaj.BellBooks.model.shipment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
public class Note {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private NoteType noteType;

    private String content;

    private LocalDate date;
}
