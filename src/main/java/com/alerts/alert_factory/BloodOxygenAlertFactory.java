package com.alerts.alert_factory;

import com.alerts.BasicAlert;
import com.alerts.BloodOxygenAlert;

public class BloodOxygenAlertFactory extends AlertFactory {
    @Override
    public BasicAlert createAlert(String patientId, String condition, long timeStamp) {
        return new BloodOxygenAlert(patientId, condition, timeStamp);
    }

}
