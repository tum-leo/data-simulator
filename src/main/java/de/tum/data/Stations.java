package de.tum.data;

import de.tum.models.Station;

import java.util.ArrayList;
import java.util.List;

public class Stations {

    public static final List<Station> stations = new ArrayList<>();

    static {
        stations.add(Station.builder().name("Karlsplatz (Stachus)").longitude("48.139560").latitude("11.564941").build());
        stations.add(Station.builder().name("Sendlinger Tor").longitude("48.133773").latitude("11.567291").build());
        stations.add(Station.builder().name("Wittelsbacherbrücke").longitude("48.122391").latitude("11.568282").build());
        stations.add(Station.builder().name("Musemumsinsel").longitude("48.130412").latitude("11.583036").build());
        stations.add(Station.builder().name("Muffatwerk").longitude("48.133010").latitude("11.588806").build());
        stations.add(Station.builder().name("Eisbachwelle").longitude("48.143339").latitude("11.587607").build());
        stations.add(Station.builder().name("Chinesischerturm").longitude("48.152437").latitude("11.591864").build());
        stations.add(Station.builder().name("Siegstor").longitude("48.152266").latitude("11.582067").build());
        stations.add(Station.builder().name("Technische Universität (Stadt)").longitude("48.148691").latitude("11.568705").build());
        stations.add(Station.builder().name("Technische Universität (Garching)").longitude("48.263418").latitude("11.669933").build());
        stations.add(Station.builder().name("Allianz Arena").longitude("48.216976").latitude("11.624931").build());
    }

}
