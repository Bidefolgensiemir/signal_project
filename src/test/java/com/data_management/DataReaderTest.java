package com.data_management;

import org.junit.jupiter.api.Test;

import com.data_management.DataReaderFromFile;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

public class DataReaderTest {

    @Test
    void testReadDataFromFile() throws java.io.IOException {
        // Create a temporary file for testing
        java.io.File tempFile = java.io.File.createTempFile("test_data", ".txt");
        try (java.io.PrintWriter out = new java.io.PrintWriter(tempFile)) {
            out.println("1, 100.0, ECG, 1714376789050");
            out.println("1, 120.0, BloodPressure, 1714376789051");
            out.println("2, 80.0, HeartRate, 1714376789052");
        }

        DataStorage storage = new DataStorage();
        DataReaderFromFile reader = new DataReaderFromFile(tempFile.getAbsolutePath());
        
        reader.readData(storage);

        java.util.List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        org.junit.jupiter.api.Assertions.assertEquals(2, records.size());
        org.junit.jupiter.api.Assertions.assertEquals(100.0, records.get(0).getMeasurementValue());
        org.junit.jupiter.api.Assertions.assertEquals("ECG", records.get(0).getRecordType());

        java.util.List<PatientRecord> patient2Records = storage.getRecords(2, 1714376789052L, 1714376789052L);
        org.junit.jupiter.api.Assertions.assertEquals(1, patient2Records.size());
        org.junit.jupiter.api.Assertions.assertEquals(80.0, patient2Records.get(0).getMeasurementValue());

     
}
}