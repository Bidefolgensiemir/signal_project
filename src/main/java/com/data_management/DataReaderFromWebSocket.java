package com.data_management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DataReaderFromWebSocket implements DataReader {

    @Override
    public void readData(InputStream inputStream, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                readLine(line, dataStorage);
            }
        }
    }

    private void readLine(String line, DataStorage dataStorage) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            System.err.println("Invalid WebSocket message: " + line);
            return;
        }

        try {
            int patientId = Integer.parseInt(parts[0].trim());
            long timestamp = Long.parseLong(parts[1].trim());
            String recordType = parts[2].trim();
            double measurementValue = Double.parseDouble(parts[3].trim());

            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (NumberFormatException e) {
            System.err.println("Could not parse WebSocket message: " + line);
        }
    }
}
