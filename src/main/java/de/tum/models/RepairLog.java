package de.tum.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "repair_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer bike;

    private Integer mechanic;

    private Date repairTime;

}
