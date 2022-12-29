package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="author")
public class Author extends Creator{

    private String prefix;

    private String firstName;

    private String middleName;

    private String lastName;

    private String suffix;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middle) {
        this.middleName = middle;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
