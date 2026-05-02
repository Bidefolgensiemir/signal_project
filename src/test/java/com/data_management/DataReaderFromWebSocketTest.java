package com.data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class DataReaderFromWebSocketTest {

    @Test
    void readDataStoresWebSocketFormat() throws java.io.IOException {
        DataStorage storage = DataStorage.getInstance();
        DataReader reader = new DataReaderFromWebSocket();
        String data = "93,1714376789053,SystolicPressure,120.0";

        try (InputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))) {
            reader.readData(inputStream, storage);
        }

        java.util.List<PatientRecord> records = storage.getRecords(93, 1714376789053L, 1714376789053L);
        assertEquals(1, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());
        assertEquals("SystolicPressure", records.get(0).getRecordType());
    }

    @Test
    void readDataHandlesCorruptedDataGracefully() throws java.io.IOException {
        DataStorage storage = DataStorage.getInstance();
        storage.reset();
        DataReader reader = new DataReaderFromWebSocket();
        
        // Mix of valid and corrupted data lines
        String invalidData = "93,1714376789053,SystolicPressure\n" + // Missing value column
                             "NOT_A_NUMBER,1714376789053,SystolicPressure,120.0\n" + // Invalid patient ID
                             "94,1714376789054,SystolicPressure,130.0"; // Valid row

        try (InputStream inputStream = new ByteArrayInputStream(invalidData.getBytes(StandardCharsets.UTF_8))) {
            reader.readData(inputStream, storage);
        }

        // The valid row for Patient 94 should be saved successfully
        java.util.List<PatientRecord> records = storage.getRecords(94, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(130.0, records.get(0).getMeasurementValue());
        
        // The corrupted rows for Patient 93 should be ignored (no crash)
        assertEquals(0, storage.getRecords(93, 0, Long.MAX_VALUE).size());
    }
}
