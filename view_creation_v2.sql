DROP VIEW air_pressure_results;
DROP VIEW bike_stats;

CREATE VIEW air_pressure_results AS

  WITH
      air_pressure_last_repair_times AS
    (
        SELECT
          bike,
          max(repair_time) AS last_repair_time
        FROM repair_log
        WHERE
          sensor = (SELECT id
                    FROM sensors
                    WHERE NAME = 'Air Pressure')
        GROUP BY bike
    ),


      air_pressure_points AS
    (
        SELECT
          s.bike,
          s.value,
          s.timestamp,
          repair.last_repair_time,
          days_between(s.timestamp, now()) AS time
        FROM sensor_data s
          INNER JOIN air_pressure_last_repair_times repair ON repair.bike = s.bike
        WHERE
          sensor = (SELECT id
                    FROM sensors
                    WHERE NAME = 'Air Pressure')
          --             AND s.bike = 48602
          AND TIMESTAMP >= repair.last_repair_time
        ORDER BY TIMESTAMP DESC
    ),


      air_pressure_bike_info AS (
        SELECT
          r.bike,
          t.minimum_air_pressure,
          t.initial_air_pressure,
          days_between(r.last_repair_time, now()) AS days_since_last_repair
        FROM air_pressure_last_repair_times r
          INNER JOIN bikes b ON r.bike = b.id
          INNER JOIN bike_types t ON b.bike_type = t.id
        ORDER BY r.last_repair_time ASC

    ),


      air_pressure_mean_estimates AS
    (SELECT
       bike,
       AVG(time * 1.)  AS xmean,
       AVG(value * 1.) AS ymean
     FROM air_pressure_points pd
     GROUP BY bike
    ),

      air_pressure_stdev_estimates AS
    (SELECT
       pd.bike
       -- T-SQL STDEV() implementation is not numerically stable
       ,
       CASE SUM(POWER(time - xmean, 2))
       WHEN 0
         THEN 1
       ELSE SQRT(SUM(POWER(time - xmean, 2)) / (COUNT(*) - 1)) END  AS xstdev,
       CASE SUM(POWER(value - ymean, 2))
       WHEN 0
         THEN 1
       ELSE SQRT(SUM(POWER(value - ymean, 2)) / (COUNT(*) - 1)) END AS ystdev
     FROM air_pressure_points pd
       INNER JOIN air_pressure_mean_estimates pm ON pm.bike = pd.bike
     GROUP BY pd.bike, pm.xmean, pm.ymean
    ),

      air_pressure_standardized_data AS -- increases numerical stability
    (SELECT
       pd.bike,
       CASE xstdev
       WHEN 0
         THEN 0
       ELSE (time - xmean) / xstdev END  AS xstd,
       CASE ystdev
       WHEN 0
         THEN 0
       ELSE (value - ymean) / ystdev END AS ystd
     FROM air_pressure_points pd
       INNER JOIN air_pressure_stdev_estimates ps ON ps.bike = pd.bike
       INNER JOIN air_pressure_mean_estimates pm ON pm.bike = pd.bike
    ),


      air_pressure_standardized_beta_estimates AS
    (SELECT
       bike,
       CASE WHEN SUM(xstd * xstd) = 0
         THEN 0
       WHEN COUNT(*) - 1 = 0
         THEN 0
       ELSE SUM(xstd * ystd) / (COUNT(*) - 1) END AS betastd
     FROM air_pressure_standardized_data
     GROUP BY bike
    ),

      air_pressure_regression AS (

        SELECT
          pb.bike,
          CASE xstdev
          WHEN 0
            THEN 1
          ELSE
            ymean - xmean * betastd * ystdev / xstdev END AS Alpha,
          CASE xstdev
          WHEN 0
            THEN 1
          ELSE betastd * ystdev / xstdev END              AS Beta,
          CASE ystdev
          WHEN 0
            THEN 1
          ELSE betastd * betastd END                      AS R2,
          betastd                                         AS Correl,
          betastd * xstdev * ystdev                       AS Covar
        FROM air_pressure_standardized_beta_estimates pb
          INNER JOIN air_pressure_stdev_estimates ps ON ps.bike = pb.bike
          INNER JOIN air_pressure_mean_estimates pm ON pm.bike = pb.bike
    ),

      bike_current_air_pressure_values AS
    (
        SELECT
          s.bike,
          s.timestamp,
          s.VALUE
        FROM (
               SELECT
                 bike,
                 max(timestamp) AS timestamp
               FROM sensor_data
               WHERE
                 sensor = (SELECT id
                           FROM sensors
                           WHERE NAME = 'Air Pressure')
               GROUP BY bike) l INNER JOIN
          sensor_data s ON l.bike = s.bike AND l.timestamp = s.timestamp
    )

  SELECT
    r.bike,
    i.days_since_last_repair,
    i.minimum_air_pressure,
    i.initial_air_pressure,
    v.value                                           AS current_value,
    CASE r.Beta
    WHEN 0
      THEN 99
    ELSE
      (v.value - i.minimum_air_pressure) / r.Beta END AS repair_needed_in_days,
    r.Beta
  FROM air_pressure_bike_info i
    INNER JOIN (SELECT *
                FROM air_pressure_regression
                WHERE Beta <> 0) r ON r.bike = i.bike
    INNER JOIN bike_current_air_pressure_values v ON v.bike = i.bike;


DROP VIEW stations_distances;

CREATE VIEW stations_distances AS

  WITH stations_geodata AS (
      SELECT
        id,
        name,
        latitude,
        longitude,
        new st_point(TO_DECIMAL(latitude), TO_DECIMAL(longitude)).ST_SRID(4326).ST_TRANSFORM(4326) AS coordinates
      FROM stations
  )

  SELECT
    s1.id                                                   AS start_station_id,
    s1.name                                                 AS start_station_name,
    s2.id                                                   AS end_station_id,
    s2.name                                                 AS end_station_name,
    s1.coordinates.st_distance(s2.coordinates, 'kilometer') AS distance
  FROM stations_geodata s1, stations_geodata s2;



DROP VIEW dist_per_bike_type_per_day;

CREATE VIEW dist_per_bike_type_per_day AS

  WITH
      distance_per_bike AS (
        SELECT
          l.bike            AS bike,
          sum(dis.distance) AS distance_sum
        FROM lending_log l
          JOIN stations_distances dis
            ON l.start_station = dis.start_station_id AND l.end_station = dis.end_station_id
          JOIN bikes b ON l.bike = b.id
        GROUP BY l.bike
    ),

      days_per_bike AS (
        SELECT
          bike                                 AS bike,
          days_between(min(start_date), now()) AS days_total
        FROM lending_log
        GROUP BY bike
    ),

      dist_per_bike_per_day AS (
        SELECT
          l.bike                         AS bike,
          dd.days_total                  AS days_total,
          l.distance_sum                 AS distance_sum,
          l.distance_sum / dd.days_total AS avg_dist
        FROM distance_per_bike l
          INNER JOIN days_per_bike dd ON l.bike = dd.bike)

  SELECT
    b.bike_type   AS bike_type,
    avg(avg_dist) AS avg_dist
  FROM dist_per_bike_per_day db
    INNER JOIN bikes b ON db.bike = b.id
  GROUP BY bike_type;


DROP VIEW wearing_results;

CREATE VIEW wearing_results AS

  WITH
      wearing_last_repair_times AS (
        SELECT
          bike             AS bike,
          max(repair_time) AS last_repair_time
        FROM repair_log
        WHERE
          sensor = (SELECT id
                    FROM sensors
                    WHERE NAME = 'Wearing')
        GROUP BY bike
    ),
      lending_log_since_last_lending AS (
        SELECT
          l.bike          AS bike,
          l.start_station AS start_station,
          l.end_station   AS end_station
        FROM lending_log l
          INNER JOIN wearing_last_repair_times repair ON repair.bike = l.bike
        WHERE
          l.START_DATE >= repair.last_repair_time
        ORDER BY l.START_DATE DESC
    ),
      lending_log_distances AS (
        SELECT
          l.bike       AS bike,
          dis.distance AS distance
        FROM lending_log_since_last_lending l
          INNER JOIN stations_distances dis
            ON l.start_station = dis.start_station_id AND l.end_station = dis.end_station_id
    )

  SELECT
    bike          AS bike,
    sum(distance) AS wearing_since_last_repair
  FROM
    lending_log_distances
  GROUP BY bike;

DROP VIEW wearing_forecast;

CREATE VIEW wearing_forecast AS

  SELECT
    b.id                                                             AS bike,
    b.bike_type,
    wr.wearing_since_last_repair,
    (ty.wearing_kilometer - wr.wearing_since_last_repair) / avg_dist AS days_left
  FROM dist_per_bike_type_per_day pd
    INNER JOIN bikes b ON b.bike_type = pd.bike_type
    INNER JOIN bike_types ty ON ty.id = b.bike_type
    INNER JOIN wearing_results wr ON wr.bike = b.id;

CREATE VIEW bike_stats AS

  WITH last_lendings AS (
      SELECT
        bike,
        MAX(end_date) AS last_lending_date
      FROM lending_log
      GROUP BY bike
  ),
      last_repairs AS (
        SELECT
          bike,
          MAX(repair_time) AS last_repair_date
        FROM repair_log
        GROUP BY bike
    ),
      current_locations AS (
        SELECT
          s.name AS current_location,
          l.bike AS bike
        FROM stations s
          JOIN
          (SELECT
             l1.ID,
             l1.end_station,
             l2.bike
           FROM lending_log l1,
             last_lendings l2
           WHERE l1.bike = l2.bike AND l1.end_date = l2.last_lending_date) AS l ON s.ID = l.end_station
    )

  SELECT
    r.bike,
    r.days_since_last_repair,
    r.minimum_air_pressure,
    r.initial_air_pressure,
    r.current_value,
    r.repair_needed_in_days,
    r.beta,
    t.name as bike_type,
    l.last_lending_date,
    re.last_repair_date,
    cs.current_location,
    CASE
    WHEN r.current_value <= r.minimum_air_pressure
      THEN 3 --DAMAGED
    WHEN r.repair_needed_in_days <= 3
      THEN 2 --CRITICAL
    ELSE 1 --AVAILABLE
    END AS status,
    CASE
    WHEN r.current_value <= r.minimum_air_pressure
      THEN 'Air Pressure'
    WHEN r.repair_needed_in_days <= 3
      THEN 'Air Pressure'
    ELSE '-'
    END AS damage
  FROM air_pressure_results r
    INNER JOIN bikes b ON r.bike = b.id
    INNER JOIN bike_types t ON b.bike_type = t.id
    INNER JOIN last_lendings AS l ON r.bike = l.bike
    INNER JOIN last_repairs AS re ON r.bike = re.bike
    INNER JOIN current_locations AS cs ON r.bike = cs.bike;

DROP VIEW status_count;

CREATE VIEW status_count AS
  SELECT
    status        AS status,
    count(status) AS count
  FROM bike_stats
  GROUP BY status;
  
DROP VIEW repair_report;
  
CREATE VIEW repair_report AS  
	 SELECT
		TO_DATE(repair_time) AS date,
		COUNT(*) AS all_damages,
		SUM(CASE WHEN s.name LIKE 'Air Pressure' THEN 1 ELSE 0 END) AS airpressure_count,
		SUM(CASE WHEN s.name LIKE 'Wearing' THEN 1 ELSE 0 END) AS wearing_count 
	FROM repair_log r
	INNER JOIN sensors s on r.sensor = s.id
	GROUP BY TO_DATE(REPAIR_TIME) 
	ORDER BY DATE;



