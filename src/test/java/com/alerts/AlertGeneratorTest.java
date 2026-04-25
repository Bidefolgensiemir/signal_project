package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlertGeneratorTest {

    @Test
    void testBloodPressureAlerts() {
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        int patientId = 1;

        // Test high Systolic BP (> 180)
        storage.addPatientData(patientId, 190.0, "SystolicBP", System.currentTimeMillis());
        
        Patient patient = storage.getAllPatients().get(0);
        
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }
    @Test
    void testLowOxygenAlert() {
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        int patientId = 2;

        // Test low SpO2 (< 92)
        storage.addPatientData(patientId, 90.0, "Saturation", System.currentTimeMillis());

        Patient patient = storage.getAllPatients().get(0);
        
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testHypotensiveHypoxemiaTrigger(){
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        int patientId = 3;
        long now = System.currentTimeMillis();

        storage.addPatientData(patientId,85.0, "SystolicBP", now);
        storage.addPatientData(patientId,91.0, "Saturation", now);

        Patient patient = storage.getAllPatients().get(0);

        assertDoesNotThrow(()-> alertGenerator.evaluateData(patient));
    }

    @Test
    void testHealthyPatientNoAlerts() {
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        int patientId = 4;
        long now = System.currentTimeMillis();

        // Values strictly within normal ranges
        storage.addPatientData(patientId, 120.0, "SystolicBP", now);
        storage.addPatientData(patientId, 80.0, "DiastolicBP", now);
        storage.addPatientData(patientId, 98.0, "Saturation", now);

        Patient patient = storage.getAllPatients().get(0);
        
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
    }

    @Test
    void testRapidDropSpO2() {
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        int patientId = 10;
        long now = System.currentTimeMillis();

        // helathy 
        storage.addPatientData(patientId, 98.0, "Saturation", now - 300000); // 5 mins ago
        
        storage.addPatientData(patientId, 92.0, "Saturation", now);

        Patient patient = storage.getAllPatients().get(0);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
}
    
}
