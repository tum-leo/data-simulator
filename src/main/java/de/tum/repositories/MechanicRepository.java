package de.tum.repositories;

import de.tum.models.Mechanic;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MechanicRepository extends CrudRepository<Mechanic, Integer> {
    List<Mechanic> findAll();
}
