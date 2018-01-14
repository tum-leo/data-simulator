package de.tum.repositories;

import de.tum.models.SensorData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SensorDataRepository extends CrudRepository<SensorData, Integer> {
    List<SensorData> findAll();
}
