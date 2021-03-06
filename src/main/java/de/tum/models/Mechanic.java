package de.tum.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "mechanics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mechanic {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;

}
