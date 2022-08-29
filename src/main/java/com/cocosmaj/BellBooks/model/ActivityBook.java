package com.cocosmaj.BellBooks.model;

import javax.persistence.*;

@Entity(name="activity_book")
public class ActivityBook extends PackageContent {

    private ActivityBookType type;
}
