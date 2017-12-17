package de.tum.simulators;

import de.tum.data.Stations;
import de.tum.models.Customer;
import de.tum.util.ModelAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MasterDataProvider extends ModelAware {

    public void provide() {
        log.debug("Provide Customers");
        provideCustomers();
        provideBikeTypes();
        provideBikes();
        provideStations();

    }

    private void provideCustomers() {
        customers.save(Customer.builder().name("my first customer").build());
    }

    private void provideBikeTypes() {

    }

    private void provideBikes() {

    }

    private void provideStations() {
        stations.save(Stations.stations);
    }

}
