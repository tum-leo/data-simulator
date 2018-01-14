package de.tum.repositories;

import de.tum.models.LendingLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LendingLogRepository extends CrudRepository<LendingLog, Integer> {

    List<LendingLog> findAll();
}
