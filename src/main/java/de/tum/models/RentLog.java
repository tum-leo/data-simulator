package de.tum.models;

public class RentLog extends Table{

    public RentLog(){
        super(7); // Columns: 1. RentID Integer 2. BikeID Integer 3. CustomerID Integer 4.StartStation Integer 5. EndStation Integer 6. StartTime DateTime 7. EndTime DateTime
    }
}
