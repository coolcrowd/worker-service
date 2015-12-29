package edu.ipd.kit.crowdcontrol.workerservice.crowdplatform;

import spark.Request;

/**
 * @author LeanderK
 * @version 1.0
 */
public interface Platform {
    /**
     * this method gets called when the client does not provide a workerID for the database.
     * @param request the request
     * @return a workerID for the database
     */
    int handleNoWorkerID(Request request);

    /**
     * this method gets called when a worker finished all the assignment.
     * Can be used to maintain the database etc.
     * @param request the request
     */
    void workerFinished(Request request);
}
