package de.tum.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class DatabaseHelper {

    @Autowired
    private EntityManager em;

    @Autowired
    private DataSource dataSource;

    private static final int BATCH_SIZE = 10000;
    
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

//    @Transactional
    public <T> void bulkSave(Collection<T> entities) {

        this.batchInsert(entities);

        int i = 0;
        int currentBatch = 0;
        int size = entities.size();

        List<T> batchList = new ArrayList<>();

        int batchesTotal = size / BATCH_SIZE;
        for (T t : entities) {
//            em.persist(t);
            batchList.add(t);
            i++;
            if (i % BATCH_SIZE == 0) {
//                em.flush();
//                em.clear();
                batchInsert(batchList);
                batchList.clear();
                logProgress(currentBatch, batchesTotal, size - i);
                currentBatch++;
            }
        }
        batchInsert(batchList);
//        em.flush();
//        em.clear();
    }

    private void logProgress(int currentBatch, int batchesTotal, int entitiesLeft) {
        int perc = Math.round(((float) currentBatch) / ((float) batchesTotal) * 100f);
//        if (perc % 2 == 0 && perc != lastBatch)
        log.debug("Processing... {}, {} entities left", perc + "%", entitiesLeft);
        lastBatch = perc;
    }


    private void batchInsert(Collection<?> items) {

        if (items.size() == 0)
            return;

        String tableName = items.stream().findFirst().get().getClass().getDeclaredAnnotation(Table.class).name();

        SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource).withTableName(tableName).usingGeneratedKeyColumns("id");
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(items.toArray());

        insert.executeBatch(batch);

    }

}
