package de.tum.data;

import de.tum.models.Sensor;

import java.util.ArrayList;
import java.util.List;

public class Sensors {

    public static final List<Sensor> sensors = new ArrayList<>();

    static {
        sensors.add(Sensor.builder().name("Air Pressure").build());
        sensors.add(Sensor.builder().name("Wearing").build());
    }
}
