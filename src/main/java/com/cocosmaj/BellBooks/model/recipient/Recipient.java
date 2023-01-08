package com.cocosmaj.BellBooks.model.recipient;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
public class Recipient {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String firstName;

    private String middleName;

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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(String assignedId) {
        this.assignedId = assignedId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @JsonManagedReference
    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public List<SpecialRequest> getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(List<SpecialRequest> specialRequests) {
        this.specialRequests = specialRequests;
    }
}
