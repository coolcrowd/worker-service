package edu.ipd.kit.crowdcontrol.workerservice.crowdplatform;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PlatformsRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PlatformNotFoundException;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PlatformOperations;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Platforms {
    private Map<String, Platform> platforms = new HashMap<>();
    private final PlatformOperations platformOperations;

    public Platforms(PlatformOperations platformOperations) {

        this.platformOperations = platformOperations;
    }

    public int handleNoWorkerID(Request request, String platformName) throws PlatformNotFoundException {
        return getPlatformOrThrow(platformName).handleNoWorkerID(request);
    }

    public void workerFinished(Request request, String platformName) throws PlatformNotFoundException {
        getPlatformOrThrow(platformName).workerFinished(request);
    }

    public boolean needsEmail(String platformName) throws PlatformNotFoundException {
        return !getPlatformRecordOrThrow(platformName).getNativePayment();
    }

    public boolean needsCalibration(String platformName) throws PlatformNotFoundException {
        return !getPlatformRecordOrThrow(platformName).getNativeQualifications();
    }

    private Platform getPlatformOrThrow(String platformName) throws PlatformNotFoundException {
        Platform platform = platforms.get(platformName);
        if (platform != null) {
            return platform;
        } else {
            throw new PlatformNotFoundException(platformName);
        }
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
