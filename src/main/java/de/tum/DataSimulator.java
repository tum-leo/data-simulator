package de.tum;

import de.tum.simulators.MasterDataProvider;
import de.tum.simulators.TimeMachine;
import de.tum.util.ModelAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class DataSimulator extends ModelAware {

    @Autowired
    private MasterDataProvider masterDataProvider;
    @Autowired
    private TimeMachine timeMachine;

    @PostConstruct
    public void initSimulation(){

        // Clear Database
        clearDatabase();

        /*
            SIMULATION START
         */

        masterDataProvider.provide();
        timeMachine.start();

        /*
            SIMULATION END
         */

        log.debug("Simulation ended. Application will now shut down...");
    }

    private void clearDatabase() {
        log.debug("Clear database");

        stations.deleteAll();
        customers.deleteAll();
        bikeTypes.deleteAll();
        bikes.deleteAll();
        mechanics.deleteAll();
        sensors.deleteAll();
        lendingLogs.deleteAll();
        repairLogs.deleteAll();
        sensorDataValues.deleteAll();

    }

}
