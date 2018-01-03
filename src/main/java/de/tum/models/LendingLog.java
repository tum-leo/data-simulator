package de.tum.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "lending_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LendingLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer customer;

    private Integer bike;

    private Integer startStation;

    private Integer endStation;

    private Date startDate;

    private Date endDate;

}
