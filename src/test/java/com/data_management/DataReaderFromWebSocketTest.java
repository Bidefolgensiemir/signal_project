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
}
