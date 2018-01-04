package de.tum.simulators;


import de.tum.models.*;
import de.tum.util.Interval;
import de.tum.util.ModelAware;
import de.tum.util.sensorConfig.AirPressure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class TimeMachine extends ModelAware{

    private Date currentSimulationDate;
    private Date endSimulationDate;

    private SimpleDateFormat dF;
    private Random random;

    private List<Bike> bikeList;
    private List<Customer> customerList;
    private List<Mechanic> mechanicList;
    private List<LendingLog> lendingLogList;
    private List<SensorData> sensorDataList;
    private List<RepairLog> repairLogList;
    private List<Station> stationList;
    private List<BikeType> bikeTypeList;
    private List<Sensor> sensorList;


    public void start(){

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

        while(currentSimulationDate.before(endSimulationDate) || currentSimulationDate.equals(endSimulationDate)){
            log.debug("Simulate date: " + dF.format(currentSimulationDate));

            this.simulateNextDay();

            currentSimulationDate = DateUtils.addDays(currentSimulationDate, 1);
        }

        this.storeData();
    }

    private void loadMasterData(){
        bikeList = (List<Bike>) bikes.findAll();
        customerList = (List<Customer>) customers.findAll();
        mechanicList = (List<Mechanic>) mechanics.findAll();
        stationList = (List<Station>) stations.findAll();
        bikeTypeList = (List<BikeType>) bikeTypes.findAll();
        sensorList = (List<Sensor>) sensors.findAll();
    }

    private void initializeLogLists(){
        lendingLogList = new ArrayList<>();
        repairLogList = new ArrayList<>();
        sensorDataList = new ArrayList<>();

        this.initializeAirPressureConfig();
    }

    private void initializeAirPressureConfig(){

        for(Bike bike : bikeList){
            bike.setAirPressureSensor(this.createRandomAirPressureConfig(this.getType(bike).getInitialAirPressure()));
        }
    }

    private BikeType getType(Bike bike){

        for(BikeType typ : bikeTypeList){
            if(typ.getId() == bike.getBikeType()){
                return typ;
            }
        }
        return null;
    }

    private AirPressure createRandomAirPressureConfig(Double initialAirPressure){

        Random random = new Random();

        Double tempFP = random.nextDouble()/10;

        return AirPressure.builder()
                .flatProbability(tempFP)
                .initialAirPressure(initialAirPressure)
                .currentAirPressure(initialAirPressure)
                .reducingValueInterval(this.createReducingValueInterval())
                .valueStartingPointInterval(this.createStartingPointInterval())
                .build();
    }

    private Interval createReducingValueInterval(){

        Random random = new Random();

        Double lL = random.nextDouble()/10;
        Double uL = lL + (random.nextDouble()/10);

        return Interval.builder()
                .lowerLimit(lL)
                .upperLimit(uL)
                .build();
    }

    private Interval createStartingPointInterval(){

        Random random = new Random();

        Double lL = (random.nextDouble() - 0.5)/10;
        Double uL = lL + (random.nextDouble()/10);

        return Interval.builder()
                .lowerLimit(lL)
                .upperLimit(uL)
                .build();
    }

    private void storeData(){
        lendingLogs.save(lendingLogList);
        repairLogs.save(repairLogList);
        sensorDataValues.save(sensorDataList);
    }

    private void simulateNextDay(){
        this.simulateBikeData();
        this.simulateLendingData();
        this.simulateRepairData();
    }

    private void simulateBikeData(){
        for(Bike bike : bikeList){
            this.updateNewAirPressure(bike);
        }
    }

    private void updateNewAirPressure(Bike bike){
        AirPressure tempAirPressure = bike.getAirPressureSensor();

        if(random.nextDouble() < tempAirPressure.getFlatProbability()){
            tempAirPressure.setCurrentAirPressure(0.0);
            this.createSensorValueLog(this.getSensorID("Air Pressure"), bike.getId(), 0.0, currentSimulationDate);
        }
        else{
            this.reduceAirPressure(tempAirPressure, bike.getId());
        }
    }

    private void reduceAirPressure(AirPressure airPressure, Integer bikeID){
        Double tempValue = airPressure.getCurrentAirPressure();

        Interval tempInterval = airPressure.getValueStartingPointInterval();
        tempValue = tempValue + (tempInterval.getLowerLimit() + (tempInterval.getUpperLimit() - tempInterval.getLowerLimit()) * random.nextDouble());

        tempInterval = airPressure.getReducingValueInterval();
        tempValue = tempValue - (tempInterval.getLowerLimit() + (tempInterval.getUpperLimit() - tempInterval.getLowerLimit()) * random.nextDouble());

        airPressure.setCurrentAirPressure(tempValue);
        this.createSensorValueLog(this.getSensorID("Air Pressure"), bikeID, tempValue, currentSimulationDate);
    }

    private void createSensorValueLog(Integer sensorID, Integer bikeID, Double sensorValue, Date timestamp){
        sensorDataList.add(SensorData.builder()
                    .sensor(sensorID)
                    .bike(bikeID)
                    .value(sensorValue)
                    .timestamp(timestamp)
                    .build());
    }

    private void simulateLendingData(){
        LendingLog tempLendingLog;
        for(int i = 0; i < config.numberLending; i++){
            tempLendingLog = this.createLendingLog();
            if(tempLendingLog != null) lendingLogList.add(tempLendingLog);
        }
    }

    private LendingLog createLendingLog(){

        Collections.shuffle(customerList);
        Collections.shuffle(bikeList);

        Integer tempBikeId = 0;

        Date tempStartDate = DateUtils.addSeconds(currentSimulationDate, random.nextInt(86400));

        while(!this.bikeIsAvailable(bikeList.get(tempBikeId), tempStartDate)){
            tempBikeId++;
            if(tempBikeId > bikeList.size()){
                return null;
            }
        }

        return LendingLog.builder()
                .bike(bikeList.get(tempBikeId).getId())
                .customer(customerList.get(0).getId())
                .startStation(stationList.get(random.nextInt(stationList.size())).getId())
                .endStation(stationList.get(random.nextInt(stationList.size())).getId())
                .startDate(tempStartDate)
                .endDate(DateUtils.addSeconds(tempStartDate, random.nextInt(config.maximumLendingHours * 60 * 60)))
                .build();
    }

    private Boolean bikeIsAvailable(Bike bike, Date startDate){
        Boolean isAvailable = true;

        isAvailable = bike.getAirPressureSensor().getCurrentAirPressure() > this.getType(bike).getMinimumAirPressure() ? isAvailable : false;
        isAvailable = this.lastBikeLending(bike).before(startDate) ? isAvailable : false;

        return isAvailable;
    }

    private Date lastBikeLending(Bike bike){

        Date tempDate = null;
        try {
            tempDate = dF.parse(config.timeStartDate);
            tempDate = DateUtils.addDays(tempDate,-1);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("Could not parse config.timeStartDate: " + config.timeStartDate);
        }

        for(LendingLog lendingLog : lendingLogList){
            if(lendingLog.getBike() == bike.getId() && lendingLog.getEndDate().after(tempDate)){
                tempDate = lendingLog.getEndDate();
            }
        }

        return tempDate;
    }

    private void simulateRepairData(){

        Date tempRepairTimeStamp = DateUtils.addSeconds(currentSimulationDate, 24*60*60-1);
        Collections.shuffle(mechanicList);

        Integer[] sensorIDs;

        for(Bike bike : bikeList){
            sensorIDs = this.bikeRequiresRepair(bike, tempRepairTimeStamp);
            if(sensorIDs != null) this.repairBike(bike, tempRepairTimeStamp, sensorIDs);
        }
    }

    private Integer[] bikeRequiresRepair(Bike bike, Date repairDate){
        ArrayList<Integer> sensorsToRepair = new ArrayList<>();

        if(repairDate.after(this.lastBikeLending(bike))){
            if(bike.getAirPressureSensor().getCurrentAirPressure() <=  this.getType(bike).getMinimumAirPressure()) sensorsToRepair.add(this.getSensorID("Air Pressure"));
            if(this.getBikeKilometer(bike, repairDate, this.lastBikeRepair(bike, this.getSensorID("Wearing"))) >= this.getType(bike).getWearingKilometer()) sensorsToRepair.add(this.getSensorID("Wearing"));
        }

        if(sensorsToRepair.isEmpty()) return null;

        return sensorsToRepair.toArray(new Integer[sensorsToRepair.size()]);
    }

    private void repairBike(Bike bike, Date repairTime, Integer[] sensorIDs){
        bike.getAirPressureSensor().setCurrentAirPressure(this.getType(bike).getInitialAirPressure());
        this.createSensorValueLog(this.getSensorID("Air Pressure"), bike.getId(), this.getType(bike).getInitialAirPressure(), repairTime);

        Integer mechanicID = mechanicList.get(random.nextInt(mechanicList.size())).getId();

        for(Integer sensor : sensorIDs){
            repairLogList.add(RepairLog.builder()
                    .bike(bike.getId())
                    .mechanic(mechanicID)
                    .repairTime(repairTime)
                    .sensor(sensor)
                    .build());
        }
    }

    private Double getBikeKilometer(Bike bike, Date repairDate,Date lastWearingRepair){

        Double kilometer = 0.0;

        for(LendingLog lendingLog : lendingLogList){
            if(lendingLog.getBike() == bike.getId() && repairDate.after(lastWearingRepair)){
                kilometer += this.getLendingLogKilometer(lendingLog);
            }
        }
        return kilometer;
    }

    private Double getLendingLogKilometer(LendingLog lendingLog){
        return (lendingLog.getEndDate().getTime() - lendingLog.getStartDate().getTime())/1000 * (config.avgDrivingKPH/(60*60));
    }

    private Integer getSensorID(String name){

        for(Sensor sensor : sensorList){
            if(name.contentEquals(sensor.getName())){
                return sensor.getId();
            }
        }
        log.error("Sensor name '" + name + "' not found");
        return null;
    }

    private Date lastBikeRepair(Bike bike, Integer sensorID){

        Date tempDate = null;
        try {
            tempDate = dF.parse(config.timeStartDate);
            tempDate = DateUtils.addDays(tempDate,-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(RepairLog repairLog : repairLogList){
            if(repairLog.getBike() == bike.getId() && repairLog.getSensor() == sensorID && repairLog.getRepairTime().after(tempDate)){
                tempDate = repairLog.getRepairTime();
            }
        }
        return tempDate;
    }

}
