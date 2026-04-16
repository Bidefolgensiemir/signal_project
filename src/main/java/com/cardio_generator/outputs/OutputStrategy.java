package com.cardio_generator.outputs;

/**
 * @author Hanna Serafin
 * defines an interface for different data output methods
 * this makes it pssible to keep simulator independent from data's
 * destination (e.g. file, network socket, console)
 */
public interface OutputStrategy {

/**
 * outputs patient data to the destination defined by implementation
 * @param patientId unique id for each patient (positive integer)
 * @param timestamp time when the data was generated in miliseconds
 * @param label category of data
 * @param data formatted string representation of the data value
 */
    void output(int patientId, long timestamp, String label, String data);
}
