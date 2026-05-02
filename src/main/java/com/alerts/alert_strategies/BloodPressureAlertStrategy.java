package com.alerts.alert_strategies;
import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureAlertStrategy implements AlertStrategy{
    @Override 
    public String checkAlert(Patient patient){
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equals("SystolicBP")) {
                if (value > 180 || value < 90) {
                    return "urgent: systolic blood pressure out of range!! value: " + value;
                            
                }
            }
            if (type.equals("DiastolicBP")) {
                if (value > 120 || value < 60) {
                    return "urgent: diastolic blood pressure out of range!! value: " + value;
                }
            }
        }
         return null;
    }
}

