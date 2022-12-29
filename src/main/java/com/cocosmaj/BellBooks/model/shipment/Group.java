package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.Entity;

@Entity(name="group")
public class Group extends Creator{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
