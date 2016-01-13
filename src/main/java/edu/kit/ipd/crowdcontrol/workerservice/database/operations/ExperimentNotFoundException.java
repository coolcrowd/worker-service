package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

/**
 * this exception signals that the no experiment for the passed experimentID exists in the database.
 * @author LeanderK
 * @version 1.0
 */
public class ExperimentNotFoundException extends RuntimeException {
    /**
     * creates a new ExperimentNotFoundException
     * @param experiment the id not existing in the database
     */
    public ExperimentNotFoundException(int experiment) {
        super("error, experiment not found: " + experiment);
    }
}
