package de.tum.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Timer {

    private long startTime;
    private long endTime;

    public void start() {
        log.debug("Start timer");
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public void printTime() {
        log.debug("Simulation execution time: {}", convertSecondsToHMmSs(endTime - startTime));
    }

    private static String convertSecondsToHMmSs(long milliseconds) {
        long seconds = milliseconds / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h,m,s) + " | Milliseconds total: " + milliseconds;
    }

}
