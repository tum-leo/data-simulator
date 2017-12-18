package de.tum.repositories;

import de.tum.models.Customer;
import de.tum.models.Mechanic;
import org.springframework.data.repository.CrudRepository;

public interface MechanicRepository extends CrudRepository<Mechanic, Integer> {

}
