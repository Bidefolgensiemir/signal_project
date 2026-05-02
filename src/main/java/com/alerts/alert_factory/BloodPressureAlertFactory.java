package com.alerts.alert_factory;

import com.alerts.Alert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timeStamp) {
        return new Alert(patientId, condition, timeStamp);
    }

}
