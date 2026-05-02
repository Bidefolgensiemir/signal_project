package com.alerts.alert_strategies;
import java.util.List;

import com.alerts.BasicAlert;
import com.alerts.alert_factory.AlertFactory;
import com.alerts.alert_factory.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureAlertStrategy implements AlertStrategy{
    
    private final AlertFactory alertFactory = new BloodPressureAlertFactory();
@Override 
    public BasicAlert checkAlert(Patient patient){
        
        
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            long timeStamp = record.getTimestamp();
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equals("SystolicBP")) {
                if (value > 180 || value < 90) {
                    return alertFactory.createAlert(patient.getPatientID(), "Low or to High Blood Pressure", timeStamp);
                 
                            
                }
            }
            if (type.equals("DiastolicBP")) {
                if (value > 120 || value < 60) {
                    return alertFactory.createAlert(patient.getPatientID(), "Low or to High Blood Pressure", timeStamp);
                }
            
        }
    }
         return null;
    

    }
}