package de.tum.repositories;

import de.tum.models.LendingLog;
import org.springframework.data.repository.CrudRepository;

public interface LendingLogRepository extends CrudRepository<LendingLog, Integer> {

}
