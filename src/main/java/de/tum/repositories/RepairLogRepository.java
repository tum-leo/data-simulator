package de.tum.repositories;

import de.tum.models.RepairLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepairLogRepository extends CrudRepository<RepairLog, Integer> {
    List<RepairLog> findAll();
}
