package edu.ipd.kit.crowdcontrol.workerservice.crowdplatform;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PlatformsRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PlatformNotFoundException;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PlatformOperations;
import spark.Request;

import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Platforms {
    private final PlatformOperations platformOperations;

    public Platforms(PlatformOperations platformOperations) {
        this.platformOperations = platformOperations;
    }

    public Optional<Integer> handleNoWorkerID(Request request, String platformName) throws PlatformNotFoundException {
        //TODO!
        return Optional.empty();
    }

    public boolean needsEmail(String platformName) throws PlatformNotFoundException {
        return !getPlatformRecordOrThrow(platformName).getNeedsEmail();
    }

    public boolean needsCalibration(String platformName) throws PlatformNotFoundException {
        return !getPlatformRecordOrThrow(platformName).getNativeQualifications();
    }

    private PlatformsRecord getPlatformRecordOrThrow(String platformName) throws PlatformNotFoundException {
        PlatformsRecord platform = platformOperations.getPlatform(platformName);
        if (platform != null) {
            return platform;
        } else {
            throw new PlatformNotFoundException(platformName);
        }
    }
}
