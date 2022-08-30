package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.*;
import java.util.Date;

@Entity(name="comic")
public class Comic extends PackageContent {

    private String issueNumber;

    private Date date;

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
