package com.alerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.alerts.BasicAlert;
import com.alerts.BloodOxygenAlert;
import com.alerts.BloodPressureAlert;
import com.alerts.ECGAlert;
import com.alerts.alert_factory.AlertFactory;
import com.alerts.alert_factory.BloodOxygenAlertFactory;
import com.alerts.alert_factory.BloodPressureAlertFactory;
import com.alerts.alert_factory.ECGAlertFactory;
import com.alerts.alert_factory.HypotensiveHypoxemiaAlertFactory;

public class AlertFactoryTest {

    @Test
    public void testBloodOxygenAlertFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        BasicAlert alert = factory.createAlert(1, "Low Saturation", 123456789L);
        
        assertTrue(alert instanceof BloodOxygenAlert, "Factory should return BloodOxygenAlert instance");
        assertEquals(1, alert.getPatientID());
        assertEquals("Low Saturation", alert.getCondition());
        assertEquals(123456789L, alert.getTimestamp());
    }

    @Test
    public void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        BasicAlert alert = factory.createAlert(2, "High BP", 987654321L);
        
        assertTrue(alert instanceof BloodPressureAlert, "Factory should return BloodPressureAlert instance");
        assertEquals(2, alert.getPatientID());
        assertEquals("High BP", alert.getCondition());
        assertEquals(987654321L, alert.getTimestamp());
    }

    @Test
    public void testECGAlertFactory() {
        AlertFactory factory = new ECGAlertFactory();
        BasicAlert alert = factory.createAlert(3, "Abnormal ECG", 1122334455L);
        
        assertTrue(alert instanceof ECGAlert, "Factory should return ECGAlert instance");
        assertEquals(3, alert.getPatientID());
        assertEquals("Abnormal ECG", alert.getCondition());
        assertEquals(1122334455L, alert.getTimestamp());
    }

    @Test
    public void testHypotensiveHypoxemiaAlertFactory() {
        AlertFactory factory = new HypotensiveHypoxemiaAlertFactory();
        BasicAlert alert = factory.createAlert(4, "Hypotensive Hypoxemia", 999999999L);
        
        assertTrue(alert != null, "Factory should return an instance of BasicAlert (or its subclass)");
        assertEquals(4, alert.getPatientID());
        assertEquals("Hypotensive Hypoxemia", alert.getCondition());
        assertEquals(999999999L, alert.getTimestamp());
    }
}