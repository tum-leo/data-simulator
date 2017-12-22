package de.tum.util.sensorConfig;

import de.tum.util.Interval;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirPressure {

    @Getter
    @Setter
    private Double initialAirPressure;
    @Getter
    @Setter
    private Double currentAirPressure;
    @Getter
    @Setter
    private Double flatProbability;
    @Getter
    @Setter
    private Interval valueStartingPointInterval;
    @Getter
    @Setter
    private Interval reducingValueInterval;


    public String toString(){
        String ret = new String();

        ret += "InitialAirPressure: " + this.initialAirPressure;
        ret += "\nCurrentAirPressure: " + this.currentAirPressure;
        ret += "\nFlatProbability: " + this.flatProbability;
        ret += "\nValueStartingPointIntervall: " + this.valueStartingPointInterval.toString();
        ret += "\nReducingValueInterval: " + this.reducingValueInterval.toString();

        return ret;
    }

}
