package de.tum.data;

import de.tum.models.BikeType;

import java.util.ArrayList;
import java.util.List;

public class BikeTypes {

    public static final List<BikeType> bikeTypes = new ArrayList<>();

    static {
        bikeTypes.add(BikeType.builder().name("E-Bike").initialAirPressure(4.0).build());
        bikeTypes.add(BikeType.builder().name("City Bike").initialAirPressure(3.0).build());
    }

}
