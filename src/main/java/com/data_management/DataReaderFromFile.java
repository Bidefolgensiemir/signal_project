package com.data_management;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DataReaderFromFile implements DataReader {
    private String filePath;

    public DataReaderFromFile() {
    }

    public DataReaderFromFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void readData(InputStream inputStream, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // splits line per line first with comma as delimiter
                if (parts.length == 4) {
                    // only parses if there are 4 parts
                    int patientId = Integer.parseInt(parts[0].trim());
                    double measurementValue = Double.parseDouble(parts[1].trim());
                    String recordType = parts[2].trim();
                    long timestamp = Long.parseLong(parts[3].trim());
                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    // each has its own type of data

                }
            }
        }
    }

    public void readData(DataStorage dataStorage) throws IOException {
        if (filePath == null) {
            throw new IOException("No file path was provided.");
        }
        try (InputStream inputStream = new FileInputStream(filePath)) {
            readData(inputStream, dataStorage);
        }
    }
}
