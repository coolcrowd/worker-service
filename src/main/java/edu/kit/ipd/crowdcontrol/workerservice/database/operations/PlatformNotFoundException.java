package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

/**
 * this exception signals that no matching platform was found in the database.
 * @author LeanderK
 * @version 1.0
 */
public class PlatformNotFoundException extends RuntimeException {
    /**
     * creates a new PlatformNotFoundException
     * @param platform the name of the platform not found in the database
     */
    public PlatformNotFoundException(String platform) {
        super("error, platform not found: " + platform);
    }
}
