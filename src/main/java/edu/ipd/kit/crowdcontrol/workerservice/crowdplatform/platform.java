package edu.ipd.kit.crowdcontrol.workerservice.crowdplatform;

import spark.Request;

/**
 * @author LeanderK
 * @version 1.0
 */
public interface Platform {
    /**
     * this method should persist Worker-Data if it is a new worker or find the worker
     * in the database if he is already registered.
     * @param request the request containing all the relevant parameters
     * @return the id of the worker
     */
    int handleWorkerData(Request request);

    /**
     * returns whether the worker is already existing in the database.
     * @param request the request
     * @return the id of the worker or -1 if not existing
     */
    int existing(Request request);

    boolean needEmail(Request request);
}
