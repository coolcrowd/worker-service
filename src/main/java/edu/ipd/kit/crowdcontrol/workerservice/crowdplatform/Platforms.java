package edu.ipd.kit.crowdcontrol.workerservice.crowdplatform;

import edu.ipd.kit.crowdcontrol.workerservice.database.operations.PlatformNotFoundException;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Platforms {
    private Map<String, Platform> platforms = new HashMap<>();

    public Platforms() {

    }

    public int handleNoWorkerID(Request request, String platformName) throws PlatformNotFoundException {
        return getPlatformOrThrow(platformName).handleNoWorkerID(request);
    }

    public void workerFinished(Request request, String platformName) throws PlatformNotFoundException {
        getPlatformOrThrow(platformName).workerFinished(request);
    }

    private Platform getPlatformOrThrow(String platformName) throws PlatformNotFoundException {
        Platform platform = platforms.get(platformName);
        if (platform != null) {
            return platform;
        } else {
            throw new PlatformNotFoundException(platformName);
        }
    }
}
