package com.alerts.alert_factory;

import com.alerts.Alert;
import com.alerts.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timeStamp) {
         
        return new BloodPressureAlert(patientId, condition, timeStamp);
    }

}
