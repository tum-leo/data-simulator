package de.tum;

import de.tum.simulators.MasterDataProvider;
import de.tum.simulators.TimeMachine;
import de.tum.util.DatabaseHelper;
import de.tum.util.ModelAware;
import de.tum.util.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@Slf4j
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class DataSimulator extends ModelAware {

    @Autowired
    private MasterDataProvider masterDataProvider;

    @Autowired
    private TimeMachine timeMachine;

    @Autowired
    private DatabaseHelper databaseHelper;

    @Autowired
    private EntityManager em;

    @PostConstruct
    public void initSimulation() {

        Timer timer = new Timer();

        timer.start();

        // Clear Database
        databaseHelper.clearDatabase();

        /*
            SIMULATION START
         */

        masterDataProvider.provide();
        timeMachine.start();

        /*
            SIMULATION END
         */

        timer.stop();
        timer.printTime();

        log.debug("Simulation ended. Application will now shut down...");
    }

}
