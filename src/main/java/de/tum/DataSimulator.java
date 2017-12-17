package de.tum;

import de.tum.simulators.MasterDataProvider;
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

    @PostConstruct
    public void initSimulation(){
        // Run Simulation

        masterDataProvider.provide();

        // Shutdown Application
        log.debug("Simulation ended. Application will now shut down...");
//        applicationContext.close();
    }

}
