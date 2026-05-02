package com.alerts.alert_factory;

import com.alerts.Alert;

public abstract class AlertFactory {
public abstract  Alert createAlert(String patientId, String condition, long timeStamp);
}
