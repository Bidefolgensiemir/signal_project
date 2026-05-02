package com.data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataStorageConcurrencyTest {

    @BeforeEach
    void setUp() {
        // ensures a clean slate before every single @Test
        DataStorage.getInstance().reset();
    }

    @Test
    void testConcurrentAdditionsToSamePatient() throws InterruptedException {
        DataStorage storage = DataStorage.getInstance();
        int patientId = 999;
        int numberOfThreads = 100;
        
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final int value = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    storage.addPatientData(patientId, value, "ConcurrentTest", System.currentTimeMillis());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        
        boolean completed = finishLatch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        java.util.List<PatientRecord> records = storage.getRecords(patientId, 0, Long.MAX_VALUE);
        assertEquals(numberOfThreads, records.size(), "Should have exactly 100 records despite concurrent writes.");
    }
    
}
