package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity(name="comic")
public class Comic extends PackageContent {

    private String issueNumber;

    private LocalDate date;

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
