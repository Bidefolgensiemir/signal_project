package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hanna Serafin
 * implementation of {@code OutputStrategy} that transofrms patients data into text file
 * each data label is stored in its own file within a specific directory
 */
public class FileOutputStrategy implements OutputStrategy { //classnames capitalised!!

    private String baseDirectory; //variables uncapitalized

    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>(); //camelCase

    /**
     * constructs a FileOutputStrategy with specific target directory
     * @param baseDirectory path where the output files will be created
     */
    public FileOutputStrategy(String baseDirectory) { //same as class

        this.baseDirectory = baseDirectory;
    }

    /**
     * creates a single data entry to a file named after the given label 
     * if they dont exist, files are created. Otherwise they are just added 
     * @param patientId unique id for each patient (positive integer)
     * @param timestamp time when the data was generated in miliseconds
     * @param label category of data (used as a file name)
     * @param data specific value to record
     * @throws RuntimeRxception if the file cant be wrriten / direcotry cant be created
     */
    @Override
    
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (IOException e) { //should throw IOException insted of normal broad Exception
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}