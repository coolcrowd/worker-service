package edu.kit.ipd.crowdcontrol.workerservice.objectservice;

import java.util.Map;
import java.util.Optional;
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
     * submits the worker.
     * Calls 'PUT /workers' from the Object-Service.
     * @param email the email to save
     * @param platform the current platform
     * @return an completable future representing the request with the resulting workerID
     */
    public CompletableFuture<Integer> putWorker(String email, String platform) {
        return null;
    }

    /**
     * submits an answer for the worker
     * Calls 'PUT /populations/answers' from the Object-Service.
     * @param answer the answer to submit
     * @param task the task answered
     * @param worker the worker answered
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitAnswer(String answer, int task, int worker) {
        return null;
    }

    /**
     * submits an rating for the worker.
     * Calls 'PUT /populations/ratings' from the Object-Service.
     * @param rating the rating to submit
     * @param task the task worked on
     * @param worker the worker responsible
     * @param answer the rated answer
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitRating(int rating, int task, int answer, int worker) {
        return null;
    }

    /**
     * submits an answer to a calibration.
     * Calls 'PUT /populations/answers/:worker/:id' from the Object-Service.
     * @param option the option chosen
     * @param worker the worker answered
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitCalibration(int option, int worker) {
        return null;
    }

    /**
     * tries to get the workerID from the request.
     * Calls 'GET /getWorkerID/:platform' from the Object-Service.
     * @param platform the current platform
     * @param queryParameter the passed query-Parameter
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Optional<Integer>> tryGetWorkerID(String platform, Map<String, String[]> queryParameter) {
        return null;
    }
}
