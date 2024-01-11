package com.cocosmaj.BellBooks.model.recipient;

import com.cocosmaj.BellBooks.model.enums.SpecialRequestCategory;
import com.cocosmaj.BellBooks.model.enums.SpecialRequestStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class SpecialRequest {

    @Id
    @GeneratedValue
    private Long id;

    private String volunteerName;

    private String request;

    private LocalDate specialRequestDate;

    private LocalDate letterMailedDate;

    @Enumerated(EnumType.STRING)
    private SpecialRequestCategory category;

    @Enumerated(EnumType.STRING)
    private SpecialRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;

    @JsonBackReference
    public Recipient getRecipient() {
        return recipient;
    }
}