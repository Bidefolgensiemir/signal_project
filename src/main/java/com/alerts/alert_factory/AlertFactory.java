package com.alerts.alert_factory;

import com.alerts.BasicAlert;

public abstract class AlertFactory {
public abstract  BasicAlert createAlert(String patientId, String condition, long timeStamp);
}
