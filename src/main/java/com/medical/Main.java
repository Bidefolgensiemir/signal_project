package com.medical;

import java.io.IOException;
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
        DataStorage storage = new DataStorage();
        try {
            com.data_management.DataReader reader = new com.data_management.DataReaderFromFile("data/patient_data.csv");
            System.out.println("reading patient data...");
            reader.readData(storage);
        
        } catch (java.io.IOException e) {
        System.err.println("CRITICAL ERROR: could not read patient data file. " + e.getMessage());
        return;
        }
        
        com.alerts.AlertGenerator alertGenerator = new com.alerts.AlertGenerator(storage);

        System.out.println("evaluating alerts for " + storage.getAllPatients().size() + " patients...");
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }

}
