package de.tum.models;


import de.tum.util.sensorConfig.AirPressure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bikes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bike {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer bikeType;

    @Transient
    private AirPressure airPressureSensor;

    @Transient
    private Integer kilometerCount;

}
