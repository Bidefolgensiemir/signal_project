package com.alerts.alert_factory;

import com.alerts.BasicAlert;
import com.alerts.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public BasicAlert createAlert(int patientId, String condition, long timeStamp) {
         
        return new BloodPressureAlert(patientId, condition, timeStamp);
    }

}
