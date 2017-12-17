package de.tum;

import de.tum.models.Customer;
import de.tum.util.ModelAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DataSimulator extends ModelAware {

    @PostConstruct
    public void initSimulation(){

    }

    private void addMasterData() {
        customers.save(new Customer("First Customer"));
    }

}
