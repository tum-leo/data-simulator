package de.tum.datamodel.Tables;

import java.util.ArrayList;
import java.util.Arrays;

public class Sensors extends Table {
    public Sensors(){
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
