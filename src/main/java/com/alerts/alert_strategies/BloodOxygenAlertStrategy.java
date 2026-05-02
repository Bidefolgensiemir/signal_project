package com.alerts.alert_strategies;

import java.util.ArrayList;
import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodOxygenAlertStrategy implements AlertStrategy{
    @Override 
    public String checkAlert(Patient patient){
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        List<Double> spo2History = new ArrayList<>();
        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equals("Saturation")) {
                 spo2History.add(value);
                if (value < 92) {
                    return "urgent: saturation out of range!! value: " + value + "%";
                }   
            }
            if (spo2History.size() > 1) {
            double currentSpO2 = spo2History.get(spo2History.size() - 1);
                for (double prev : spo2History) {
                    if (prev - currentSpO2 >= 5.0) {
                        return "trend: rapid spO2 drop!";
                    }
                }
            }
        }
        return null;
    }
}
