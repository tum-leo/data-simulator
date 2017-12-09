package de.tum.datamodel;

import de.tum.datamodel.Tables.*;

import java.util.ArrayList;

public class DataModel {

    ArrayList<Object> dataModel;

    public DataModel(){
        this.dataModel = createTables();
    }

    private ArrayList<Object> createTables(){

        ArrayList<Object> tables = new ArrayList<Object>();

        tables.add(new Fleet());
        tables.add(new Sensors());
        tables.add(new SensorData());
        tables.add(new Customer());
        tables.add(new RentLog());
        tables.add(new RepairLog());
        tables.add(new Station());

        return tables;
    }
}
