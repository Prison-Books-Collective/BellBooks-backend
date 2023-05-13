package com.cocosmaj.BellBooks.model.shipment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name="zine")
@DiscriminatorValue("zine")
public class Zine extends PackageContent {

    private String code;
}
