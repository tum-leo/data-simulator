package de.tum;

import de.tum.models.Customer;
import de.tum.util.ModelAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class DataSimulator extends ModelAware {

    @PostConstruct
    public void initSimulation(){
        // Run Simulation

        // TODO Simualtion
        addMasterData();

        // Shutdown Application
        log.debug("Simulation ended. Application will now shut down...");
//        applicationContext.close();
    }

    private void addMasterData() {
        Customer c1 = new Customer();
        c1.setName("hello again");
        customers.save(c1);

        for (Customer customer : customers.findAll()) {
            log.debug(customer.getName());
        }
    }

}
