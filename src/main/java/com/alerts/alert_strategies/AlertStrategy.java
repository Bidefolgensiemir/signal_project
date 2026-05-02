package com.alerts.alert_strategies;

import com.data_management.Patient;
import com.alerts.*;
public interface  AlertStrategy {
     // null if there is no alert, otherwise string for each category
    BasicAlert checkAlert (Patient patient);
}

