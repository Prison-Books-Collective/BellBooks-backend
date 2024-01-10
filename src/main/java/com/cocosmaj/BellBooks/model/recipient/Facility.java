package com.cocosmaj.BellBooks.model.recipient;

import com.cocosmaj.BellBooks.model.enums.State;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "facility", uniqueConstraints = {
    @UniqueConstraint(name = "uniqueFacility", columnNames = {
        "name", "street", "state", "zip"
    })
})
public class Facility {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false, length = 100)
    private String name;

    private String additionalInfo;

    @NotNull
    @Column(nullable = false, length = 100)
    private String street;

    @NotNull
    @Column(nullable = false, length = 100)
    private String city;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @NotNull
    @Column(nullable = false, length=10)
    private String zip;
}
