package com.alerts;

public class ECGAlert extends BasicAlert {
    public ECGAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}