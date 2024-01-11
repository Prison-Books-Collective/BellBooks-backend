package com.cocosmaj.BellBooks.model.shipment;

import com.cocosmaj.BellBooks.model.recipient.Facility;
import com.cocosmaj.BellBooks.model.recipient.Recipient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Shipment {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;

    @OneToMany
    private List<Note> notes;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<PackageContent> content;

    @SuppressWarnings("unused")
    @JsonBackReference
    public Recipient getRecipient() {
        return recipient;
    }
}
