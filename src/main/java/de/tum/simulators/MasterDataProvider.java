package de.tum.simulators;

import de.tum.data.BikeTypes;
import de.tum.data.Stations;
import de.tum.models.Bike;
import de.tum.models.BikeType;
import de.tum.models.Customer;
import de.tum.models.Mechanic;
import de.tum.util.FakeData;
import de.tum.util.ModelAware;
import io.codearte.jfairy.producer.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MasterDataProvider extends ModelAware {

    public void provide() {
        provideCustomers();
        provideBikeTypes();
        provideBikes();
        provideStations();
        provideMechanics();

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
        log.debug("Provide Bike Types");

        bikeTypes.save(BikeTypes.bikeTypes);

    }

    private void provideBikes() {

        log.debug("Provide Bikes");

        List<Bike> bikeList = new ArrayList<>();

        List<BikeType> types = BikeTypes.bikeTypes;

        for (int i = 0; i < config.numberBikes; i++) {
            Collections.shuffle(types);
            bikeList.add(
                    Bike.builder()
                            .bikeType(types.get(0).getId())
                            .build()
            );
        }

        bikes.save(bikeList);
    }

    private void provideStations() {
        log.debug("Provide Stations");

        stations.save(Stations.stations);
    }

    private void provideMechanics(){
        log.debug("Provide Mechanics");

        List<Mechanic> mechanicList = new ArrayList<>();

        for (int i = 0; i < config.numberMechanics; i++) {
            Person person = FakeData.getPerson();
            mechanicList.add(
                    Mechanic.builder()
                            .firstName(person.getFirstName())
                            .lastName(person.getLastName())
                            .build()
            );
        }

        mechanics.save(mechanicList);
    }

}
