package edu.ipd.kit.crowdcontrol.workerservice.objectservice;

import java.util.concurrent.CompletableFuture;

/**
 * this class is used to communicate with the object-Service
 * @author LeanderK
 * @version 1.0
 */
public class Communication {
    private final String url;
    public Communication(String url) {
        this.url = url;
    }

    //TODO
    public CompletableFuture<String> submitEmail(int Worker, String email) {
        return null;
    }

    public CompletableFuture<Integer> submitEmail(String email, String platform) {
        return null;
    }

    public CompletableFuture<Integer> submitAnswer(String Answer, int task, int worker) {
        return null;
    }

    public CompletableFuture<Integer> submitRating(int rating, int task, int worker, int answer) {
        return null;
    }

    public CompletableFuture<Integer> submitCalibration(int option, int worker) {
        return null;
    }
}
