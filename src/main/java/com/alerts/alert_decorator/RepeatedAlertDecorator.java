package com.alerts.alert_decorator;

import com.alerts.Alert;

/**
 * Decorator that marks an alert as repeated.
 * This decorator wraps an existing Alert and appends "(Repeated Alert)" to the condition.
 */
public class RepeatedAlertDecorator implements Alert {

    private final Alert decoratedAlert;

    /**
     * Constructs a RepeatedAlertDecorator with the specified alert.
     *
     * @param decoratedAlert the alert to be decorated
     */
    public RepeatedAlertDecorator(Alert decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }

    @Override
    public int getPatientID() {
        return decoratedAlert.getPatientID();
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " (Repeated Alert)";
    }

    @Override
    public long getTimestamp() {
        return decoratedAlert.getTimestamp();
    }
}
