package com.alerts.alert_factory;

import com.alerts.Alert;
import com.alerts.ECGAlert;

public class ECGAlertFactory extends AlertFactory {
   @Override
    public Alert createAlert(String patientId, String condition, long timeStamp) {
        return new ECGAlert(patientId, condition, timeStamp);
    }
}
