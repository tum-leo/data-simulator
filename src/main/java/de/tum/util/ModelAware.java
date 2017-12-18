package de.tum.util;

import de.tum.SimulatorConfig;
import de.tum.repositories.CustomerRepository;
import de.tum.repositories.MechanicRepository;
import de.tum.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ModelAware {

    @Autowired
    protected SimulatorConfig config;

    @Autowired
    protected CustomerRepository customers;

    @Autowired
    protected StationRepository stations;

    @Autowired
    protected MechanicRepository mechanics;

}
