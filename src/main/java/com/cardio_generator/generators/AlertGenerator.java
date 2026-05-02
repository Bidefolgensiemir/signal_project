package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alert data for patients in a cardiovascular monitoring system.
 *
 * <p>The {@code AlertGenerator} class simulates the triggering and resolution of alerts
 * for patients based on a probabilistic model. It implements the
 * {@link PatientDataGenerator} interface to generate alert events and output them using
 * the provided {@link OutputStrategy}.
 *
 * <p>This generator uses a Poisson process model to simulate alert events with a
 * configurable average rate (lambda). Each alert has a high probability (90%) of being
 * resolved in the next cycle if it has been triggered.
 *
 * <p><b>Alert States:</b>
 * <ul>
 *   <li>{@code false} - No active alert (normal state)</li>
 *   <li>{@code true} - Active alert (in alert state)</li>
 * </ul>
 *
 * @author Cardio Generator System
 * @version 1.0
 * @since 2026-04-26
 */
public class AlertGenerator implements PatientDataGenerator {

  /**
   * Static random number generator used for probabilistic alert generation.
   * This generator is shared across all instances of {@code AlertGenerator}.
   */
  private static Random randomGenerator = new Random();

  /**
   * Array tracking the alert state for each patient.
   * Index corresponds to patient ID; {@code true} indicates an active alert,
   * {@code false} indicates normal state.
   */
  private boolean[] alertStates;

  /**
   * Constructs an {@code AlertGenerator} for the specified number of patients.
   *
   * <p>Initializes the alert state array with dimensions sufficient to track
   * alerts for patients with IDs from 0 to {@code patientCount}.
   *
   * @param patientCount the total number of patients to generate alerts for
   * @throws IllegalArgumentException if {@code patientCount} is negative
   */
  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  /**
   * Generates alert data for a specific patient and outputs it using the provided
   * strategy.
   *
   *
   * @param patientId    the unique identifier of the patient for which to generate alert data
   * @param outputStrategy the strategy to use for outputting the generated alert data
   * @throws ArrayIndexOutOfBoundsException if {@code patientId} exceeds the
   *         initialized patient count
   * @throws NullPointerException if {@code outputStrategy} is null
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        // Patient is in alert state - attempt to resolve
        if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
        }
      } else {
        // Patient is in normal state - check if new alert should be triggered
        double lambda = 0.1; // Average rate (alerts per period)
        // Probability of at least one alert in the period
        double p = -Math.expm1(-lambda);
        boolean alertTriggered = randomGenerator.nextDouble() < p;

        if (alertTriggered) {
          alertStates[patientId] = true;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
        }
      }
    } catch (Exception e) {
      System.err.println("An error occurred while generating alert data for patient " + patientId);
      e.printStackTrace();
    }
  }
}
