package de.tum.simulators;


import de.tum.models.*;
import de.tum.util.DatabaseHelper;
import de.tum.util.HashCollection;
import de.tum.util.Interval;
import de.tum.util.ModelAware;
import de.tum.util.sensorConfig.AirPressure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TimeMachine extends ModelAware {

    @Autowired
    DatabaseHelper databaseHelper;

    private Date currentSimulationDate;
    private Date endSimulationDate;

    private SimpleDateFormat dF;
    private Random random;

    private Map<Integer, Bike> bikeList;
    private HashCollection<Bike, LendingLog> lendingLogList;
    private List<SensorData> sensorDataList;
    private HashCollection<Bike, RepairLog> repairLogList;
    private List<Station> stationList;
    private List<BikeType> bikeTypeList;
    private List<Sensor> sensorList;

    // helper for randomization
    private Bike[] bikeArray;
    private Customer[] customerArray;
    private Mechanic[] mechanicArray;

    public void start() {

        dF = new SimpleDateFormat("dd/MM/yyyy");
        random = new Random();

        this.loadMasterData();
        this.initializeLogLists();

        try {
            currentSimulationDate = dF.parse(config.timeStartDate);
            endSimulationDate = dF.parse(config.timeEndDate);

        } catch (ParseException e) {
            log.error("Simulation start or end date parsing error. System shut down");
            System.exit(1);
        }

        while (currentSimulationDate.before(endSimulationDate) || currentSimulationDate.equals(endSimulationDate)) {
            log.debug("Simulate date: " + dF.format(currentSimulationDate));

            this.simulateNextDay();

            currentSimulationDate = DateUtils.addDays(currentSimulationDate, 1);
        }

        this.storeData();
    }

    private void loadMasterData() {
        bikeList = bikes.findAll().stream().collect(Collectors.toMap(Bike::getId, b -> b));
        List<Customer> customerList = customers.findAll();
        List<Mechanic> mechanicList = mechanics.findAll();
        stationList = stations.findAll();
        bikeTypeList = bikeTypes.findAll();
        sensorList = sensors.findAll();

        bikeArray = Arrays.copyOf(bikeList.values().toArray(), bikeList.size(), Bike[].class);
        customerArray = Arrays.copyOf(customerList.toArray(), customerList.size(), Customer[].class);
        mechanicArray = Arrays.copyOf(mechanicList.toArray(), mechanicList.size(), Mechanic[].class);
    }

    private void initializeLogLists() {
        lendingLogList = new HashCollection<>();
        repairLogList = new HashCollection<>();
        sensorDataList = new ArrayList<>();

        this.initializeAirPressureConfig();
    }

    private void initializeAirPressureConfig() {

        for (Bike bike : bikeList.values()) {
            bike.setAirPressureSensor(this.createRandomAirPressureConfig(this.getType(bike).getInitialAirPressure()));
        }
    }

    private BikeType getType(Bike bike) {

        for (BikeType typ : bikeTypeList) {
            if (typ.getId().equals(bike.getBikeType())) {
                return typ;
            }
        }
        return null;
    }

    private AirPressure createRandomAirPressureConfig(Double initialAirPressure) {

        Random random = new Random();

        Double tempFP = random.nextDouble() / 100;

        return AirPressure.builder()
                .flatProbability(tempFP)
                .initialAirPressure(initialAirPressure)
                .currentAirPressure(initialAirPressure)
                .reducingValueInterval(this.createReducingValueInterval())
                .valueStartingPointInterval(this.createStartingPointInterval())
                .build();
    }

    private Interval createReducingValueInterval() {

        Random random = new Random();

        Double lL = random.nextDouble() / 10;
        Double uL = lL + (random.nextDouble() / 10);

        return Interval.builder()
                .lowerLimit(lL)
                .upperLimit(uL)
                .build();
    }

    private Interval createStartingPointInterval() {

        Random random = new Random();

        Double lL = (random.nextDouble() - 0.5) / 10;
        Double uL = lL + (random.nextDouble() / 10);

        return Interval.builder()
                .lowerLimit(lL)
                .upperLimit(uL)
                .build();
    }

    private void storeData() {
        log.debug("Store lendingLog in db... {} entries", lendingLogList.values().size());
        databaseHelper.bulkSave(lendingLogList.values());

        log.debug("Store repairLog in db... {} entries", repairLogList.values().size());
        databaseHelper.bulkSave(repairLogList.values());

        log.debug("Store sensorValues in db... {} entries", sensorDataList.size());
        databaseHelper.bulkSave(sensorDataList);
    }

    private void simulateNextDay() {
        this.simulateBikeData();
        this.simulateLendingData();
        this.simulateRepairData();
    }

    private void simulateBikeData() {
        for (Bike bike : bikeList.values()) {
            this.updateNewAirPressure(bike);
        }
    }

    private void updateNewAirPressure(Bike bike) {
        AirPressure tempAirPressure = bike.getAirPressureSensor();

        if (random.nextDouble() < tempAirPressure.getFlatProbability()) {
            tempAirPressure.setCurrentAirPressure(0.0);
            this.createSensorValueLog(this.getSensorID("Air Pressure"), bike.getId(), 0.0, currentSimulationDate);
        } else {
            this.reduceAirPressure(tempAirPressure, bike.getId());
        }
    }

    private void reduceAirPressure(AirPressure airPressure, Integer bikeID) {
        Double tempValue = airPressure.getCurrentAirPressure();

        Interval tempInterval = airPressure.getValueStartingPointInterval();
        tempValue = tempValue + (tempInterval.getLowerLimit() + (tempInterval.getUpperLimit() - tempInterval.getLowerLimit()) * random.nextDouble());

        tempInterval = airPressure.getReducingValueInterval();
        tempValue = tempValue - (tempInterval.getLowerLimit() + (tempInterval.getUpperLimit() - tempInterval.getLowerLimit()) * random.nextDouble());

        airPressure.setCurrentAirPressure(tempValue);
        this.createSensorValueLog(this.getSensorID("Air Pressure"), bikeID, tempValue, currentSimulationDate);
    }

    private void createSensorValueLog(Integer sensorID, Integer bikeID, Double sensorValue, Date timestamp) {
        sensorDataList.add(SensorData.builder()
                .sensor(sensorID)
                .bike(bikeID)
                .value(sensorValue)
                .timestamp(timestamp)
                .build());
    }

    private void simulateLendingData() {
        LendingLog tempLendingLog;
        for (int i = 0; i < config.numberLending; i++) {
            tempLendingLog = this.createLendingLog();
            if (tempLendingLog != null) lendingLogList.put(bikeList.get(tempLendingLog.getBike()), tempLendingLog);
        }
    }

    private LendingLog createLendingLog() {

        Date tempStartDate = DateUtils.addSeconds(currentSimulationDate, random.nextInt(86400));

        Bike tempBike = getRandomBike();

        while (!this.bikeIsAvailable(tempBike, tempStartDate)) {
            tempBike = getRandomBike();
        }

        return LendingLog.builder()
                .bike(tempBike.getId())
                .customer(getRandomCustomer().getId())
                .startStation(stationList.get(random.nextInt(stationList.size())).getId())
                .endStation(stationList.get(random.nextInt(stationList.size())).getId())
                .startDate(tempStartDate)
                .endDate(DateUtils.addSeconds(tempStartDate, random.nextInt(config.maximumLendingHours * 60 * 60)))
                .build();
    }

    private Boolean bikeIsAvailable(Bike bike, Date startDate) {

        boolean isNotBroken = bike.getAirPressureSensor().getCurrentAirPressure() > this.getType(bike).getMinimumAirPressure();
        Date lastBikeLending = this.lastBikeLending(bike);
        boolean isAvailable = lastBikeLending == null || lastBikeLending.before(startDate);

        return isNotBroken && isAvailable;
    }

    private Date lastBikeLending(Bike bike) {

        Date tempDate = null;
        try {
            tempDate = dF.parse(config.timeStartDate);
            tempDate = DateUtils.addDays(tempDate, -1);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("Could not parse config.timeStartDate: " + config.timeStartDate);
        }

        for (LendingLog lendingLog : lendingLogList.get(bike)) {
            if (lendingLog.getEndDate().after(tempDate)) {
                tempDate = lendingLog.getEndDate();
            }
        }

        return tempDate;
    }

    private void simulateRepairData() {

        Date tempRepairTimeStamp = DateUtils.addSeconds(currentSimulationDate, 24 * 60 * 60 - 1);

        Integer[] sensorIDs;

        for (Bike bike : bikeList.values()) {
            sensorIDs = this.bikeRequiresRepair(bike, tempRepairTimeStamp);
            if (sensorIDs != null) this.repairBike(bike, tempRepairTimeStamp, sensorIDs);
        }
    }

    private Integer[] bikeRequiresRepair(Bike bike, Date repairDate) {
        ArrayList<Integer> sensorsToRepair = new ArrayList<>();

        if (repairDate.after(this.lastBikeLending(bike))) {
            if (bike.getAirPressureSensor().getCurrentAirPressure() <= this.getType(bike).getMinimumAirPressure())
                sensorsToRepair.add(this.getSensorID("Air Pressure"));
            if (this.getBikeKilometer(bike, repairDate, this.lastBikeRepair(bike, this.getSensorID("Wearing"))) >= this.getType(bike).getWearingKilometer())
                sensorsToRepair.add(this.getSensorID("Wearing"));
        }

        if (sensorsToRepair.isEmpty()) return null;

        return sensorsToRepair.toArray(new Integer[sensorsToRepair.size()]);
    }

    private void repairBike(Bike bike, Date repairTime, Integer[] sensorIDs) {
        bike.getAirPressureSensor().setCurrentAirPressure(this.getType(bike).getInitialAirPressure());
        this.createSensorValueLog(this.getSensorID("Air Pressure"), bike.getId(), this.getType(bike).getInitialAirPressure(), repairTime);

        Integer mechanicID = getRandomMechanic().getId();

        for (Integer sensor : sensorIDs) {
            repairLogList.put(bike, RepairLog.builder()
                    .bike(bike.getId())
                    .mechanic(mechanicID)
                    .repairTime(repairTime)
                    .sensor(sensor)
                    .build());
        }
    }

    private Double getBikeKilometer(Bike bike, Date repairDate, Date lastWearingRepair) {

        Double kilometer = 0.0;

        for (LendingLog lendingLog : lendingLogList.get(bike)) {
            if (repairDate.after(lastWearingRepair)) {
                kilometer += this.getLendingLogKilometer(lendingLog);
            }
        }
        return kilometer;
    }

    private Double getLendingLogKilometer(LendingLog lendingLog) {
        return (lendingLog.getEndDate().getTime() - lendingLog.getStartDate().getTime()) / 1000 * (config.avgDrivingKPH / (60 * 60));
    }

    private Integer getSensorID(String name) {

        for (Sensor sensor : sensorList) {
            if (name.contentEquals(sensor.getName())) {
                return sensor.getId();
            }
        }
        log.error("Sensor name '" + name + "' not found");
        return null;
    }

    private Date lastBikeRepair(Bike bike, Integer sensorID) {

        Date tempDate = null;
        try {
            tempDate = dF.parse(config.timeStartDate);
            tempDate = DateUtils.addDays(tempDate, -1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (RepairLog repairLog : repairLogList.get(bike)) {
            if (repairLog.getSensor().equals(sensorID) && repairLog.getRepairTime().after(tempDate)) {
                tempDate = repairLog.getRepairTime();
            }
        }
        return tempDate;
    }

    private Bike getRandomBike() {
        return bikeArray[random.nextInt(bikeArray.length)];
    }

    private Customer getRandomCustomer() {
        return customerArray[random.nextInt(customerArray.length)];
    }

    private Mechanic getRandomMechanic() {
        return mechanicArray[random.nextInt(mechanicArray.length)];
    }

}
