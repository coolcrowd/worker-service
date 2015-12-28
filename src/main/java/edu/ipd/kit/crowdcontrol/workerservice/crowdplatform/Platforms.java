package edu.ipd.kit.crowdcontrol.workerservice.crowdplatform;

import spark.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Platforms {
    private Map<String, Platform> platforms = new HashMap<>();

    public Platforms() {

    }

    /**
     * persists the workerdata
     * @param request the request
     * @param platformName the name of the platform
     * @return the id of the worker
     */
    public int handleWorkerData(Request request, String platformName) {
        Platform platform = platforms.get(platformName);
        if (platform != null) {
            return platform.handleWorkerData(request);
        } else {
            return -1;
        }
    }

    /**
     * returns whether the Worker is already existing
     * @param request the request
     * @param platformName the name of the platform
     * @return the id of the worker if he is existing, -1 if the worker is not existing
     *         or empty if the platform is not existing
     */
    public Optional<Integer> isExisting(Request request, String platformName) {
        return doIfExisting(platformName, platform -> platform.existing(request));
    }

    public Optional<Boolean> needsEmail(Request request, String platformName) {
        return doIfExisting(platformName, platform -> platform.needEmail(request));
    }

    private <T> Optional<T> doIfExisting(String platformName, Function<Platform, T> func) {
        Platform platform = platforms.get(platformName);
        if (platform != null) {
            return Optional.of(func.apply(platform));
        } else {
            return Optional.empty();
        }
    }
}
