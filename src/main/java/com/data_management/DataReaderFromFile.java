package com.data_management;


public class DataReaderFromFile implements DataReader {
private String filePath;

    public DataReaderFromFile(String filePath) {
        this.filePath = filePath;
    }

    public void readData(DataStorage dataStorage) throws java.io.IOException {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //splts line per line first with comma as delimiter
                if (parts.length == 4) {
                    //only parses if there are 4 parts
                    int patientId = Integer.parseInt(parts[0].trim());
                    double measurementValue = Double.parseDouble(parts[1].trim());
                    String recordType = parts[2].trim();
                    long timestamp = Long.parseLong(parts[3].trim());
                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    //each has its own type of data

                }
            }
        }
    }



}