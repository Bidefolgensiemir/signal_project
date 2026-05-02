package com.alerts.alert_decorator;

import com.alerts.Alert;

/**
 * Decorator that adds priority level to alerts.
 * This decorator wraps an existing Alert and adds priority information.
 */
public class PriorityAlertDecorator implements Alert {

    private Alert decoratedAlert;
    private int priority; // 1 (low) to 5 (critical)

    /**
     * Constructs a PriorityAlertDecorator with the specified alert and priority.
     *
     * @param decoratedAlert the alert to be decorated
     * @param priority       the priority level (1-5)
     */
    public PriorityAlertDecorator(Alert decoratedAlert, int priority) {
        this.decoratedAlert = decoratedAlert;
        this.priority = priority;
    }

    @Override
    public int getPatientID() {
        return decoratedAlert.getPatientID();
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " [Priority: " + priority + "]";
    }

    @Override
    public long getTimestamp() {
        return decoratedAlert.getTimestamp();
    }
}
