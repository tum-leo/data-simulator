package de.tum.models;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customers")
@Builder
public class RepairLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

}
