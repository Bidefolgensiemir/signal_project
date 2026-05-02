package com.alerts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.alerts.alert_decorator.PriorityAlertDecorator;
import com.alerts.alert_decorator.RepeatedAlertDecorator;

public class AlertDecoratorTest {

    private static class MockAlert implements Alert {
        private int patientId;
        private String condition;
        private long timestamp;

        public MockAlert(int patientId, String condition, long timestamp) {
            this.patientId = patientId;
            this.condition = condition;
            this.timestamp = timestamp;
        }

        @Override
        public int getPatientID() {
            return patientId;
        }

        @Override
        public String getCondition() {
            return condition;
        }

        @Override
        public long getTimestamp() {
            return timestamp;
        }
    }

    @Test
    public void testPriorityAlertDecorator() {
        Alert baseAlert = new MockAlert(123, "Heart Rate High", 1000L);
        Alert priorityAlert = new PriorityAlertDecorator(baseAlert, 5);

        assertEquals(123, priorityAlert.getPatientID());
        assertEquals("Heart Rate High [Priority: 5]", priorityAlert.getCondition());
        assertEquals(1000L, priorityAlert.getTimestamp());
    }

    @Test
    public void testRepeatedAlertDecorator() {
        Alert baseAlert = new MockAlert(124, "Low SpO2", 2000L);
        Alert repeatedAlert = new RepeatedAlertDecorator(baseAlert);

        assertEquals(124, repeatedAlert.getPatientID());
        assertEquals("Low SpO2 (Repeated Alert)", repeatedAlert.getCondition());
        assertEquals(2000L, repeatedAlert.getTimestamp());
    }

    @Test
    public void testCombinedDecorators() {
        Alert baseAlert = new MockAlert(125, "Critical Condition", 3000L);
        Alert repeatedAlert = new RepeatedAlertDecorator(baseAlert);
        Alert priorityRepeatedAlert = new PriorityAlertDecorator(repeatedAlert, 3);

        assertEquals(125, priorityRepeatedAlert.getPatientID());
        assertEquals("Critical Condition (Repeated Alert) [Priority: 3]", priorityRepeatedAlert.getCondition());
        assertEquals(3000L, priorityRepeatedAlert.getTimestamp());
    }
}