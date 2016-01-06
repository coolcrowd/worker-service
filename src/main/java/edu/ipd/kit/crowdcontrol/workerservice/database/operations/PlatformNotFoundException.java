package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

/**
 * this exception signals that no matching platform was found in the database.
 * @author LeanderK
 * @version 1.0
 */
public class PlatformNotFoundException extends RuntimeException {
    public PlatformNotFoundException(String platform) {
        super("error, platform not found: " + platform);
    }
}
