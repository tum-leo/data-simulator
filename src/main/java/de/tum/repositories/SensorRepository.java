package de.tum.repositories;

import de.tum.models.Sensor;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, Integer> {

}
