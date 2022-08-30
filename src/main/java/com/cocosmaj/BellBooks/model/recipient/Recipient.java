package com.cocosmaj.BellBooks.model.recipient;

import com.cocosmaj.BellBooks.model.shipment.Shipment;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Recipient {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String firstName;

    @Column(nullable=false)
    private String lastName;

    private String assignedId;

    @ManyToOne
    private Facility facility;

    @OneToMany(mappedBy = "recipient")
    private List<Shipment> shipments;

    @OneToMany
    private List<SpecialRequest> specialRequests;

    public Recipient() {
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
