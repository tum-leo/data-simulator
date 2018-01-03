package de.tum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimulatorConfig {

    @Value("${simulator.customers.number}")
    public int numberCustomers;

    @Value("${simulator.mechanics.number}")
    public int numberMechanics;

    @Value("${simulator.bikes.number}")
    public int numberBikes;

    @Value("${simulator.time.startDate}")
    public String timeStartDate;

    @Value("${simulator.time.endDate}")
    public String timeEndDate;

    @Value("${simulator.lending.number}")
    public int numberLending;

    @Value("${simulator.lending.maximumLendingHours}")
    public int maximumLendingHours;
}
