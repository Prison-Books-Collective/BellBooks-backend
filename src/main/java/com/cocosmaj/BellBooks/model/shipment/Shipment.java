package com.cocosmaj.BellBooks.model.shipment;

import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Shipment {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @ManyToOne
    @JoinColumn(name="recipient_id")
    private Recipient recipient;

    @OneToMany
    private List<Note> notes;

    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @OneToMany
    private List<PackageContent> content;

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

    @JsonBackReference
    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public List<PackageContent> getContent() {
        return content;
    }

    public void setContent(List<PackageContent> content) {
        this.content = content;
    }
}
