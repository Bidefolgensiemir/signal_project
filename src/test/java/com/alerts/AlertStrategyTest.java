package com.alerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.alerts.BasicAlert;
import com.alerts.alert_strategies.AlertStrategy;
import com.alerts.alert_strategies.BloodOxygenAlertStrategy;
import com.alerts.alert_strategies.BloodPressureAlertStrategy;
import com.alerts.alert_strategies.ECGAlertStrategy;
import com.alerts.alert_strategies.HypotensiveHypoxemiaAlertStrategy;
import com.data_management.Patient;

public class AlertStrategyTest {

    @Test
    public void testBloodPressureAlertStrategy_HighSystolic() {
        Patient patient = new Patient(1);
        patient.addRecord(190.0, "SystolicBP", System.currentTimeMillis());
        
        AlertStrategy strategy = new BloodPressureAlertStrategy();
        BasicAlert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert, "Alert should be triggered for High Systolic BP");
        assertEquals("Low or to High Blood Pressure", alert.getCondition());
    }

    @Test
    public void testBloodPressureAlertStrategy_Normal() {
        Patient patient = new Patient(1);
        patient.addRecord(120.0, "SystolicBP", System.currentTimeMillis());
        patient.addRecord(80.0, "DiastolicBP", System.currentTimeMillis());
        
        AlertStrategy strategy = new BloodPressureAlertStrategy();
        BasicAlert alert = strategy.checkAlert(patient);
        
        assertNull(alert, "No alert should be triggered for normal BP");
    }

    @Test
    public void testBloodOxygenAlertStrategy_LowOxygen() {
        Patient patient = new Patient(2);
        patient.addRecord(90.0, "Saturation", System.currentTimeMillis());
        
        AlertStrategy strategy = new BloodOxygenAlertStrategy();
        BasicAlert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert, "Alert should be triggered for SpO2 below 92%");
        assertEquals("Low Blood Oxygen", alert.getCondition());
    }

    @Test
    public void testHypotensiveHypoxemiaAlertStrategy_Trigger() {
        Patient patient = new Patient(3);
        long now = System.currentTimeMillis();
        // Note: Using exact casing mapped in HypotensiveHypoxemiaAlertStrategy ("systolicBP" and "saturation")
        patient.addRecord(85.0, "systolicBP", now); 
        patient.addRecord(91.0, "saturation", now); 
        
        AlertStrategy strategy = new HypotensiveHypoxemiaAlertStrategy();
        BasicAlert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert, "Hypotensive Hypoxemia alert should be triggered when both conditions are met");
        assertEquals("Hypotensive Hypoxemia", alert.getCondition());
    }

    @Test
    public void testECGAlertStrategy_AbnormalPeak() {
        Patient patient = new Patient(4);
        long now = System.currentTimeMillis();
        // ECG Strategy requires at least 10 records to create an average
        for (int i = 0; i < 10; i++) {
            patient.addRecord(0.5, "ECG", now - (i * 1000));
        }
        patient.addRecord(1.5, "ECG", now);
        
        AlertStrategy strategy = new ECGAlertStrategy();
        BasicAlert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert, "Alert should be triggered for ECG value exceeding 1.2 threshold");
        assertEquals("ECG abnormal peak", alert.getCondition());
    }
}