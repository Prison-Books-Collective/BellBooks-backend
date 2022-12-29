package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Note {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private NoteType noteType;

    private String content;

    private LocalDate date;

    public void setId(Long newId){
        id = newId;
    }

    public void setContent(String noteContent){
        content = noteContent;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public Long getId(){
        return id;
    }

    public String getContent(){
        return content;
    }

    public LocalDate getDate(){
        return date;
    }
}
