package com.cocosmaj.BellBooks.model;

import org.hibernate.criterion.NotEmptyExpression;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Shipment {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @OneToMany
    private List<Note> notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public List<Note> getNotes(){
        return notes;
    }

    public void setNotes(List<Note> notes){
        this.notes = notes;
    }
}
