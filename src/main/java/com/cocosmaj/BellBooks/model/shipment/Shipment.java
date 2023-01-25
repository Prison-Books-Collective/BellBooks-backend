package com.cocosmaj.BellBooks.model.shipment;

import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Shipment {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="facility_id")
    private Facility facility;

    @ManyToOne
    @JoinColumn(name="recipient_id")
    private Recipient recipient;

    @OneToMany
    private List<Note> notes;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<PackageContent> content;

    public Long getId() {
        return id;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
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

    public Set<PackageContent> getContent() {
        return content;
    }

    public void setContent(Set<PackageContent> content) {
        this.content = content;
    }
}
