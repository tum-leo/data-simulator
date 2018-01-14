package de.tum.simulators;

import de.tum.data.BikeTypes;
import de.tum.data.Sensors;
import de.tum.data.Stations;
import de.tum.models.Bike;
import de.tum.models.BikeType;
import de.tum.models.Customer;
import de.tum.models.Mechanic;
import de.tum.util.DatabaseHelper;
import de.tum.util.FakeData;
import de.tum.util.ModelAware;
import io.codearte.jfairy.producer.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MasterDataProvider extends ModelAware {

    @Autowired
    private DatabaseHelper databaseHelper;

    public void provide() {
        provideCustomers();
        provideSensors();
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
        databaseHelper.bulkSave(customerList);
    }

    private void provideSensors() {

        log.debug("Provide Sensors");

        databaseHelper.bulkSave(Sensors.sensors);
    }

    private void provideBikeTypes() {

        log.debug("Provide Bike Types");

        databaseHelper.bulkSave(BikeTypes.bikeTypes);
    }

    private void provideBikes() {

        log.debug("Provide Bikes");

        List<Bike> bikeList = new ArrayList<>();

        List<BikeType> types = (List<BikeType>) bikeTypes.findAll();

        for (int i = 0; i < config.numberBikes; i++) {
            bikeList.add(this.createRandomBike(types));
        }

        databaseHelper.bulkSave(bikeList);
    }

    private void provideStations() {

        log.debug("Provide Stations");

        databaseHelper.bulkSave(Stations.stations);
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

        databaseHelper.bulkSave(mechanicList);
    }

    private Bike createRandomBike(List<BikeType> types){

        Collections.shuffle(types);

        return Bike.builder()
                .bikeType(types.get(0).getId())
                .airPressureSensor(null)
                .build();
    }



}
