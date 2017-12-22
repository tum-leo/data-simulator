package de.tum.simulators;

import de.tum.data.BikeTypes;
import de.tum.data.Stations;
import de.tum.models.Bike;
import de.tum.models.BikeType;
import de.tum.models.Customer;
import de.tum.models.Mechanic;
import de.tum.util.FakeData;
import de.tum.util.Interval;
import de.tum.util.ModelAware;
import de.tum.util.sensorConfig.AirPressure;
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

        log.debug("Truncate Customer Table");

        customers.deleteAll();

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
        log.debug("Truncate Bike Types table");

        bikeTypes.deleteAll();

        log.debug("Provide Bike Types");

        bikeTypes.save(BikeTypes.bikeTypes);

    }

    private void provideBikes() {

        log.debug("Truncate Bike table");

        bikes.deleteAll();

        log.debug("Provide Bikes");

        List<Bike> bikeList = new ArrayList<>();

        List<BikeType> types = (List<BikeType>) bikeTypes.findAll();

        for (int i = 0; i < config.numberBikes; i++) {
            bikeList.add(this.createRandomBike(types));
        }
        
        bikes.save(bikeList);
    }

    private void provideStations() {
        log.debug("Truncate Stations table");

        stations.deleteAll();

        log.debug("Provide Stations");

        stations.save(Stations.stations);
    }

    private void provideMechanics(){

        log.debug("Truncate Mechanics table");

        mechanics.deleteAll();

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

    private Bike createRandomBike(List<BikeType> types){

        Collections.shuffle(types);

        return Bike.builder()
                .bikeType(types.get(0).getId())
                .airPressureSensor(this.createRandomAirPressureConfig(types.get(0).getInitialAirPressure()))
                .build();
    }

    private AirPressure createRandomAirPressureConfig(Double initialAirPressure){

        Random random = new Random();

        Double tempFP = random.nextDouble()/10;

        return AirPressure.builder()
                .flatProbability(tempFP)
                .initialAirPressure(initialAirPressure)
                .currentAirPressure(initialAirPressure)
                .reducingValueInterval(this.createReducingValueIntverval())
                .valueStartingPointInterval(this.createStartingPointIntverval())
                .build();
    }

    private Interval createReducingValueIntverval(){

        Random random = new Random();

        Double lL = random.nextDouble()/10;
        Double uL = lL + (random.nextDouble()/10);

        return Interval.builder()
                .lowerLimit(lL)
                .upperLimit(uL)
                .build();
    }

    private Interval createStartingPointIntverval(){

        Random random = new Random();

        Double lL = (random.nextDouble() - 0.5)/10;
        Double uL = lL + (random.nextDouble()/10);

        return Interval.builder()
                .lowerLimit(lL)
                .upperLimit(uL)
                .build();
    }

}
