package com.cocosmaj.BellBooks.model.shipment;

import javax.persistence.*;

@Entity(name="zine")
public class Zine extends PackageContent {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
