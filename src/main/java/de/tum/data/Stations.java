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
        stations.add(Station.builder().name("Chinesischer Turm").longitude("48.152437").latitude("11.591864").build());
        stations.add(Station.builder().name("Siegstor").longitude("48.152266").latitude("11.582067").build());
        stations.add(Station.builder().name("Technische Universität (Stadt)").longitude("48.148691").latitude("11.568705").build());
        stations.add(Station.builder().name("Technische Universität (Garching)").longitude("48.263418").latitude("11.669933").build());
        stations.add(Station.builder().name("Allianz Arena").longitude("48.216976").latitude("11.624931").build());
        stations.add(Station.builder().name("BMW Welt").longitude("48.177613").latitude("11.555148").build());
        stations.add(Station.builder().name("Olypmpia Park").longitude("48.171457").latitude("11.547682").build());
        stations.add(Station.builder().name("Hauptbahnhof").longitude("48.141534").latitude("11.558489").build());
        stations.add(Station.builder().name("Schloss Nymphenburg").longitude("48.158148").latitude("11.504189").build());
        stations.add(Station.builder().name("Tierpark Hellabrunn").longitude("48.100505").latitude("11.551565").build());
        stations.add(Station.builder().name("Perlacher Forst").longitude("48.089373").latitude("11.574547").build());
        stations.add(Station.builder().name("Bundeswehr Universität").longitude("48.081722").latitude("11.632617").build());
        stations.add(Station.builder().name("Klinikum Großhadern").longitude("48.112574").latitude("11.468835").build());
        stations.add(Station.builder().name("Pasing Arcaden").longitude("48.148266").latitude("11.462703").build());
        stations.add(Station.builder().name("Rotkreuzplatz").longitude("48.153253").latitude("11.533126").build());
        stations.add(Station.builder().name("Ostbahnhof").longitude("48.127903").latitude("11.604084").build());
        stations.add(Station.builder().name("Messestadt West").longitude("48.132538").latitude("11.690944").build());
        stations.add(Station.builder().name("Messestadt Ost").longitude("48.132839").latitude("11.703451").build());
        stations.add(Station.builder().name("Schloss Blutenburg").longitude("48.163725").latitude("11.456907").build());

    }

}
