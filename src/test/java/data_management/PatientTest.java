package data_management;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.data_management.PatientRecord;

public class PatientTest {

    @Test
    public void getRecordsTest() {
    com.data_management.Patient patient = new com.data_management.Patient(2);
    // Record 1: Inside Range
    patient.addRecord(100.0, "ECG", 1700000000000L); 
    // Record 2: Inside Range (Boundary)
    patient.addRecord(120.0, "ECG", 1710000000000L); 
    // Record 3: Outside Range (Too late)
    patient.addRecord(130.0, "ECG", 1720000000000L); 

    // We only want records between 1700... and 1710...
    java.util.List<PatientRecord> records = patient.getRecords(1700000000000L, 1710000000000L);

    // Assertions
    assertEquals(2, records.size(), "Should only return records within the time range"); 
    assertEquals(100.0, records.get(0).getMeasurementValue());
    assertEquals(120.0, records.get(1).getMeasurementValue()); 
}

    }


