package de.tum.models;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customers")
public class Bike {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

}
