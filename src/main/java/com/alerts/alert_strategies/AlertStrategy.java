package com.alerts.alert_strategies;

import com.data_management.Patient;

public interface  AlertStrategy {
     // null if there is no alert, otherwise string for each category
    String checkAlert (Patient patient);
}

