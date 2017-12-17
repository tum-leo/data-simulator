package de.tum.models;

import java.util.ArrayList;

public class Sensor extends Table {
    public Sensor(){
        super(2); // Columns: 1. SensorID Integer 2. Name String

        this.createInitialValues();

        System.out.println(super.table);
    }


    public void createInitialValues(){

        ArrayList<Object> tmp = new ArrayList<Object>() {{
            add((Object) 0);
            add((Object)"Air Pressure");
        }};

        super.addTuple(tmp);

        tmp = new ArrayList<Object>() {{
            add((Object) 1);
            add((Object) "GPS");
        }};

        super.addTuple(tmp);
    }
}
