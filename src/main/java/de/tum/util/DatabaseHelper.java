package de.tum.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collection;

@Slf4j
@Service
public class DatabaseHelper {

    @Autowired
    EntityManager em;

    private static final int batchSize = 50;
    
    @Transactional
    public void clearDatabase() {
        log.debug("Clear database");

        log.debug("Delete stations...");
        em.createNativeQuery("TRUNCATE TABLE stations").executeUpdate();
        log.debug("Delete customers...");
        em.createNativeQuery("TRUNCATE TABLE customers").executeUpdate();
        log.debug("Delete bikeTypes...");
        em.createNativeQuery("TRUNCATE TABLE bike_types").executeUpdate();
        log.debug("Delete bikes...");
        em.createNativeQuery("TRUNCATE TABLE bikes").executeUpdate();
        log.debug("Delete mechanics...");
        em.createNativeQuery("TRUNCATE TABLE mechanics").executeUpdate();
        log.debug("Delete sensors...");
        em.createNativeQuery("TRUNCATE TABLE sensors").executeUpdate();
        log.debug("Delete lendingLogs...");
        em.createNativeQuery("TRUNCATE TABLE lending_log").executeUpdate();
        log.debug("Delete repairLogs...");
        em.createNativeQuery("TRUNCATE TABLE repair_log").executeUpdate();
        log.debug("Delete sensorDataValues...");
        em.createNativeQuery("TRUNCATE TABLE sensor_data").executeUpdate();


    }

    private int lastBatch = 0;

    @Transactional
    public <T> void bulkSave(Collection<T> entities) {
        int i = 0;
        int currentBatch = 0;
        int size = entities.size();
        int batchesTotal = size / batchSize;
        for (T t : entities) {
            em.persist(t);
            i++;
            if (i % batchSize == 0) {
                em.flush();
                em.clear();
                currentBatch++;
                logProgress(currentBatch, batchesTotal, size - i);
            }
        }
        em.flush();
        em.clear();
    }

    private void logProgress(int currentBatch, int batchesTotal, int entitiesLeft) {
        int perc = Math.round(((float) currentBatch) / ((float) batchesTotal) * 100f);
        if (perc % 10 == 0 && perc != lastBatch)
            log.debug("Processing... {}, {} entities left", perc + "%", entitiesLeft);
        lastBatch = perc;
    }
}
