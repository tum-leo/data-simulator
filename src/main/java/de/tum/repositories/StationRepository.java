package de.tum.repositories;

import de.tum.models.Station;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StationRepository extends CrudRepository<Station, Integer> {
    List<Station> findAll();
}
