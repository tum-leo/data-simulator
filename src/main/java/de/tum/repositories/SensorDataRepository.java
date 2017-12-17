package de.tum.repositories;

import de.tum.models.SensorData;
import org.springframework.data.repository.CrudRepository;

public interface SensorDataRepository extends CrudRepository<SensorData, Integer> {

}
