package com.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Proxy;
import java.net.http.WebSocket;

import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.WebSocketClient;

public class SystemIntegrationTest {
    @Test
    void testEndToEndAlertFlow() {
        
        DataStorage storage = DataStorage.getInstance();
        storage.reset(); // ensure clean state
        AlertGenerator generator = new AlertGenerator(storage);
        
        
        WebSocketClient client = new WebSocketClient("ws://localhost:8080", false);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            String criticalMessage = "1," + System.currentTimeMillis() + ",SystolicBP,200.0";
            
            WebSocket fakeWebSocket = (WebSocket) Proxy.newProxyInstance(
                    WebSocket.class.getClassLoader(),
                    new Class<?>[] { WebSocket.class },
                    (proxy, method, args) -> null);

            client.onText(fakeWebSocket, criticalMessage, true); 
            generator.generate();

            String output = outContent.toString();
            assertTrue(output.contains("!!! MEDICAL ALERT TRIGGERED !!!"), "Alert should trigger for high BP.");
            assertTrue(output.contains("Patient ID: 1"), "Alert should identify the correct patient.");
        } finally {
            System.setOut(originalOut);
        }
    }
}
