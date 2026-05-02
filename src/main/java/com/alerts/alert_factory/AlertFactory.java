package com.alerts.alert_factory;

import com.alerts.BasicAlert;

public abstract class AlertFactory {
public abstract  BasicAlert createAlert(int patientId, String condition, long timeStamp);
}
