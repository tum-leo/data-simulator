package de.tum.simulators;

import de.tum.data.Stations;
import de.tum.models.Customer;
import de.tum.util.FakeData;
import de.tum.util.ModelAware;
import io.codearte.jfairy.producer.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MasterDataProvider extends ModelAware {

    public void provide() {
        provideCustomers();
        provideBikeTypes();
        provideBikes();
        provideStations();

    }

    private void provideCustomers() {

        log.debug("Provide Customers");

        List<Customer> customerList = new ArrayList<>();

        for (int i = 0; i < config.numberCustomers; i++) {
            Person person = FakeData.getPerson();
            customerList.add(
                    Customer.builder()
                            .firstName(person.getFirstName())
                            .lastName(person.getLastName())
                            .build()
            );
        }

        customers.save(customerList);
    }

    private void provideBikeTypes() {

    }

    private void provideBikes() {

    }

    private void provideStations() {
        stations.save(Stations.stations);
    }

}
