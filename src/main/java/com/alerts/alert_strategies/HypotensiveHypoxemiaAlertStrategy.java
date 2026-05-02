package com.alerts.alert_strategies;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.alert_factory.HypotensiveHypoxemiaAlertFactory;
import java.util.List;
import com.alerts.alert_factory.AlertFactory;
import com.alerts.BasicAlert;

public class HypotensiveHypoxemiaAlertStrategy implements AlertStrategy {
    private final AlertFactory alertFactory = new HypotensiveHypoxemiaAlertFactory();

    @Override
    public BasicAlert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        
        boolean hasHypoxemia = false;
        boolean hasHypotension = false;

        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();
            long timeStamp = record.getTimestamp();


            // check for hypoxemia: oxygen saturation < 92%
            if (type.equals("saturation") && value < 92.0) {
                hasHypoxemia = true;
            }

            // check for hypotension: systolic blood pressure < 90 mmHg
            if (type.equals("systolicBP") && value < 90.0) {
                hasHypotension = true;
            }

            if (hasHypoxemia && hasHypotension) {
                return alertFactory.createAlert(patient.getPatientID(), "Hypotensive Hypoxemia", timeStamp);
            }
        }
        return null;
    }
}
