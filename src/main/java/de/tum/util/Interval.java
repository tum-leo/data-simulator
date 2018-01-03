package de.tum.util;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interval {

    @Getter
    @Setter
    private double lowerLimit;
    @Getter
    @Setter
    private double upperLimit;


    public String toString(){
        return "[" + this.lowerLimit + ", " + this.upperLimit + "]";
    }
}
