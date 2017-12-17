package de.tum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimulatorConfig {

    @Value("${simulator.customers.number}")
    public int numberCustomers;

}
