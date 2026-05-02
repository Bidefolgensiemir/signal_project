package com.medical;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;
import com.data_management.DataReader;
import com.data_management.DataReaderFromFile;
import com.data_management.Patient;
import com.data_management.WebSocketClient;
import com.alerts.AlertGenerator;


public class Main {
    public static void main(String[] args) throws IOException {

        DataStorage storage = DataStorage.getInstance();
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        String serverUri = "ws://localhost:8080";
        WebSocketClient client = new WebSocketClient(serverUri);
        System.out.println("Real-time monitoring system started...");

        while (true) {
            try {
                alertGenerator.generate();
                
                // frequency of analysis (every 2 seconds)
                Thread.sleep(2000); 
            } catch (InterruptedException e) {
                System.err.println("Monitoring loop interrupted: " + e.getMessage());
                break;
            }
        }
    }

      
}
