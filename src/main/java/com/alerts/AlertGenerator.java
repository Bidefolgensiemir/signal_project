package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        
            // FIXED: Added space before opening brace 
            // hypotensive hypoxemia
            if (lastSystolic != -1 && lastSaturation != -1) {
                // FIXED: Added space after 'if' keyword 
                if (lastSystolic < 90 && lastSaturation < 92) {
                    // FIXED: Split long line into multiple lines (line length < 100 chars)
                    triggerAlert(
                        new Alert(
                            String.valueOf(patient.getPatientID()),
                            " critical: hypotensive hypoxemia (BP: " + lastSystolic
                                + ", spO2: " + lastSaturation + "%)",
                            currentTime));
                }
            }
        }


    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("========================================");
        System.out.println("!!! MEDICAL ALERT TRIGGERED !!!");
        System.out.println("Patient ID: " + alert.getPatientId());
        System.out.println("Condition:  " + alert.getCondition());
        // FIXED: Moved Date instantiation to same line 
        System.out.println("Timestamp:  " + new java.util.Date(alert.getTimestamp()));
        System.out.println("========================================");
    }
}
