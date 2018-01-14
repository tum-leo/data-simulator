package de.tum.repositories;

import de.tum.models.BikeType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BikeTypeRepository extends CrudRepository<BikeType, Integer> {

    List<BikeType> findAll();

}
