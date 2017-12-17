package de.tum.data;

import de.tum.models.Station;

import java.util.ArrayList;
import java.util.List;

public class Stations {

    public static final List<Station> stations = new ArrayList<>();

    static {
        stations.add(Station.builder().longitude("123123").latitude("123123").build());
        stations.add(Station.builder().longitude("234234").latitude("234234").build());
    }

}
