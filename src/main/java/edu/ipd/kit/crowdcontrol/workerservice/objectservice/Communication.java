package edu.ipd.kit.crowdcontrol.workerservice.objectservice;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * this class is used to communicate with the object-Service.
 * @author LeanderK
 * @version 1.0
 */
public class Communication {
    private final String url;

    /**
     * create a new instance of Communication with the passed url to the object-service
     * @param url the url to use
     */
    public Communication(String url) {
        this.url = url;
    }

    /**
     * submits the email to the object-service
     * @param Worker the worker to submit the email for
     * @param email the email to save
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitEmail(int Worker, String email) {
        return null;
    }

    /**
     * submits the email to the object-service
     * @param platform the platform the worker is working on
     * @param email the email to save
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitEmail(String platform, String email) {
        return null;
    }

    /**
     * submits an answer for the worker
     * @param answer the answer to submit
     * @param task the task answered
     * @param worker the worker answered
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitAnswer(String answer, int task, int worker) {
        return null;
    }

    /**
     * submits an rating for the worker
     * @param rating the rating to submit
     * @param task the task worked on
     * @param worker the worker responsible
     * @param answer the rated answer
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitRating(int rating, int task, int worker, int answer) {
        return null;
    }

    /**
     * submits an answer to a calibration
     * @param option the option chosen
     * @param worker the worker answered
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitCalibration(int option, int worker) {
        return null;
    }

    /**
     * tries to get the workerID from the request
     * @param platform the current platform
     * @param queryParameter the passed query-Parameter
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> tryGetWorkerID(String platform, HashMap<String, String> queryParameter) {
        return null;
    }
}
