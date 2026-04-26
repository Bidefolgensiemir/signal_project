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
        long currentTime = System.currentTimeMillis();
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        
        double lastSystolic = -1;
        double lastSaturation = -1;
        List<Double> spo2History = new ArrayList<>();
        List<Double> ecgHistory = new ArrayList<>();

        for (PatientRecord record: records){
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if(type.equals("SystolicBP")){
                lastSystolic = value;
                if (value > 180 || value < 90){
                    triggerAlert(new Alert(String.valueOf(patient.getPatientID()), "urgent: systolic blood pressure out of range!! value: " + value, currentTime));
                }
            }
            if(type.equals("DiastolicBP")){
                if (value > 120 || value < 60){
                    triggerAlert(new Alert(String.valueOf(patient.getPatientID()), "urgent: diastolic blood pressure out of range!! value: " + value, currentTime));
                }
            }
            if (type.equals("Saturation")){
                lastSaturation = value;
                spo2History.add(value);
                if(value < 92){
                    triggerAlert(new Alert(String.valueOf(patient.getPatientID()), "urgent: saturation out of range!! value: " + value + "%", currentTime));
                }
            }
            if (type.equals("ECG")) {
                ecgHistory.add(value);
                
                // maintain sliding window (last 20 readings)
                if (ecgHistory.size() > 20) {
                    ecgHistory.remove(0);
                }

                // only evaluate if we have enough data for a meaningful average
                if (ecgHistory.size() >= 10) {
                    double sum = 0;
                    for (double d : ecgHistory) sum += d;
                    double average = sum / ecgHistory.size();

                    if (value > 1.2) {
                        triggerAlert(new Alert(String.valueOf(patient.getPatientID()), 
                            "urgent: ECG peak detected!! value: " + value, currentTime));
                    }

                    if (Math.abs(value - average) > 0.5) {
                        triggerAlert(new Alert(String.valueOf(patient.getPatientID()), 
                            "critical: ECG abnormal peak far beyond average!! value: " + value, currentTime));
                    }
                }
            }
            // hypotensive hypoxemia
            if (lastSystolic != -1 && lastSaturation != -1){
                if(lastSystolic < 90 && lastSaturation < 92){
                    triggerAlert(new Alert(String.valueOf(patient.getPatientID()), " critical: hypotensive hypoxemia (BP: " + lastSystolic + ", spO2: " + lastSaturation + "%)", currentTime));
                }
            }
        }
        // rapid drop
        if (spo2History.size() > 1) {
        double currentSpO2 = spo2History.get(spo2History.size() - 1);
        for (double prev : spo2History) {
            if (prev - currentSpO2 >= 5.0) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientID()), "trend: rapid spO2 drop!", currentTime));
                break;
                }
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
        System.out.println("Timestamp:  " + new java.util.Date(alert.getTimestamp()));
        System.out.println("========================================");
    }
}
