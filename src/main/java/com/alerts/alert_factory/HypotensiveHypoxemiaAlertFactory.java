package com.alerts.alert_factory;

import com.alerts.BasicAlert;
import com.alerts.HypotensiveHypoxemiaAlert;

public class HypotensiveHypoxemiaAlertFactory extends  AlertFactory {
    @Override
    public BasicAlert createAlert(int patientId, String condition, long timeStamp){
        return new HypotensiveHypoxemiaAlert(patientId, condition, timeStamp);
    }
}
