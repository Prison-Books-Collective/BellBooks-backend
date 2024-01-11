package com.cocosmaj.BellBooks.model.recipient;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
public class Recipient {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable=false)
    private String firstName;

    private String middleName;

    @NotNull
    @Column(nullable=false)
    private String lastName;

    @Column(unique = true)
    private String assignedId;

    @ManyToOne
    private Facility facility;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(mappedBy = "recipient")
    private List<Shipment> shipments;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany
    private List<SpecialRequest> specialRequests;

    public Recipient() {
    }

    @JsonManagedReference
    public List<Shipment> getShipments() {
        return shipments;
    }
}
