package de.tum.repositories;

import de.tum.models.Bike;
import org.springframework.data.repository.CrudRepository;

public interface BikeRepository extends CrudRepository<Bike, Integer> {

}
