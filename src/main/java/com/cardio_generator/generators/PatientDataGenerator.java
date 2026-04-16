package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/** @author Kolja Nitschke
* Interface to generate Patientdata
*Implements strategy pattern for output
*/
public interface PatientDataGenerator {

    /**
     * Generates Specific Patient Data 
     * @param patientId Unique Identifier 
     * @param outputStrategy Strategy how data is outputted 
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
