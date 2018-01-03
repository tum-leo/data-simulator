package de.tum.util;

import de.tum.SimulatorConfig;
import de.tum.repositories.*;
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

    @Autowired
    protected BikeTypeRepository bikeTypes;

    @Autowired
    protected BikeRepository bikes;

    @Autowired
    protected RepairLogRepository repairLogs;

    @Autowired
    protected LendingLogRepository lendingLogs;

    @Autowired
    protected SensorDataRepository sensorDataValues;

}
