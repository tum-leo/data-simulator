package de.tum.util;

import de.tum.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ModelAware {

    @Autowired
    protected CustomerRepository customers;

}
