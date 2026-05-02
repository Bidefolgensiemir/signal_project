package com.medical;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;
import com.data_management.DataReader;
import com.data_management.DataReaderFromFile;
import com.data_management.Patient;
import com.alerts.AlertGenerator;


public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("--simulate")) {
            System.out.println("starting healt data simulator...");
            HealthDataSimulator.main(new String[]{});
        } else {
            //HealthDataSimulator.main(new String[]{});
            startAlertSystem();
        }
    }

    private static void startAlertSystem(){
        DataStorage storage = DataStorage.getInstance();
        try {
            DataReader reader = new DataReaderFromFile();
            System.out.println("reading patient data...");
            try (InputStream inputStream = new FileInputStream("data/patient_data.csv")) {
                reader.readData(inputStream, storage);
            }
        
        } catch (java.io.IOException e) {
        System.err.println("CRITICAL ERROR: could not read patient data file. " + e.getMessage());
        return;
        }
        
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        System.out.println("evaluating alerts for " + storage.getAllPatients().size() + " patients...");
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }

}
