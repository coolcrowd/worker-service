package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

/**
 * @author LeanderK
 * @version 1.0
 */
public class ExperimentNotFoundException extends RuntimeException {
    public ExperimentNotFoundException(int experiment) {
        super("error, experiment not found: " + experiment);
    }
}
