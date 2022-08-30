package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.*;
import java.util.Date;

@Entity(name="magazine")
public class Magazine extends PackageContent {

    private String volume;

    private String issue;

    private Date date;

    @Enumerated(value= EnumType.STRING)
    private MagazineGenre genre;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MagazineGenre getGenre() {
        return genre;
    }

    public void setGenre(MagazineGenre genre) {
        this.genre = genre;
    }
}
