package com.cocosmaj.BellBooks.model.shipment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity(name="zine")
@DiscriminatorValue("zine")
public class Zine extends PackageContent {
    @NotNull
    private String code;
}
