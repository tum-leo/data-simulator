package de.tum.repositories;

import de.tum.models.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    public List<Customer> findAll();
}
