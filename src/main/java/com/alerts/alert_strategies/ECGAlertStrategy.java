package com.alerts.alert_strategies;

import java.util.ArrayList;
import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;


public class ECGAlertStrategy implements AlertStrategy{
    @Override 
    public String checkAlert(Patient patient){
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
         List<Double> ecgHistory = new ArrayList<>();

        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equals("ECG")) {
                ecgHistory.add(value);

                // maintain sliding window (last 20 readings)
                if (ecgHistory.size() > 20) {
                    ecgHistory.remove(0);
                }

                // only evaluate if we have enough data for a meaningful average
                if (ecgHistory.size() >= 10) {
                    double sum = 0;
                    for (double d : ecgHistory) {
                        sum += d;
                    }
                    double average = sum / ecgHistory.size();

                    if (value > 1.2) {
                        return "urgent: ECG peak detected!! value: " + value;
                    }

                    if (Math.abs(value - average) > 0.5) {
                        return "critical: ECG abnormal peak far beyond average!! value: " + value;
                    }
                }
            }
        }
        return null;
    }
}