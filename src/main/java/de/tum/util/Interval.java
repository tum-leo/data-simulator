package de.tum.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interval {
    private double lowerLimit;
    private double upperLimit;


    public String toString(){
        return "[" + this.lowerLimit + ", " + this.upperLimit + "]";
    }
}
