package de.tum.repositories;

import de.tum.models.Bike;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BikeRepository extends CrudRepository<Bike, Integer> {

}
