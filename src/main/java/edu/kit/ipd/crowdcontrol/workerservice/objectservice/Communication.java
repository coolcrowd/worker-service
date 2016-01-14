package edu.kit.ipd.crowdcontrol.workerservice.objectservice;

import com.googlecode.protobuf.format.JsonFormat;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import edu.kit.ipd.crowdcontrol.objectservice.proto.Answer;
import edu.kit.ipd.crowdcontrol.objectservice.proto.Rating;
import edu.kit.ipd.crowdcontrol.objectservice.proto.Worker;

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
    private final JsonFormat protobufJSON = new JsonFormat();
    private final String username;
    private final String password;

    /**
     * create a new instance of Communication with the passed url to the object-service
     * @param url the url to use
     * @param username the username to authenticate the rest-requests with
     * @param password the password to authenticate the rest-requests with
     */
    public Communication(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * submits the worker.
     * Calls 'PUT /workers' from the Object-Service.
     * @param email the email to save
     * @param platform the current platform
     * @return an completable future representing the request with the resulting workerID
     */
    public CompletableFuture<Integer> submitWorker(String email, String platform) {
        Worker worker = Worker.newBuilder()
                .setPlatform(platform)
                .setEmail(email)
                .build();

        return putRequest("/workers", builder -> builder
                .body(protobufJSON.printToString(worker))
                .asJson()
        ).thenApply(response -> {
            if (response.getStatus() == 201) {
                return response.getBody().getObject().getInt("id");
            } else if (response.getStatus() == 409) {
                return Integer.parseInt(response.getHeaders().getFirst("Location"));
            } else {
                throw new RuntimeException("illegal Response, status : " + response.getStatus());
            }
        });
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
        Answer answerProto = Answer.newBuilder()
                .setContent(answer)
                .setWorker(worker)
                .build();
        return putRequest("/experiments/"+task+"/answers", builder -> builder
                .body(protobufJSON.printToString(answerProto))
                .asJson()
        ).thenApply(response -> {
            if (response.getStatus() == 201) {
                return response.getBody().getObject().getInt("id");
            } else if (response.getStatus() == 409) {
                return Integer.parseInt(response.getHeaders().getFirst("Location"));
            } else {
                throw new RuntimeException("illegal Response, status : " + response.getStatus());
            }
        });
    }

    /**
     * submits an rating for the worker.
     * Calls 'PUT /populations/ratings' from the Object-Service.
     * @param rating the rating to submit
     * @param task the task worked on
     * @param worker the worker responsible
     * @param answer the rated answer
     * @return an completable future representing the request with the location of the answer in the database
     */
    public CompletableFuture<Integer> submitRating(int rating, int task, int answer, int worker) {
        Rating ratingProto = Rating.newBuilder()
                .setRating(rating)
                .setWorker(worker)
                .build();
        return putRequest("/experiments/"+task+"/answers/"+answer+"/ratings", builder -> builder
                .body(protobufJSON.printToString(ratingProto))
                .asJson()
        ).thenApply(response -> Integer.parseInt(response.getHeaders().getFirst("Location")));
    }

    /**
     * submits an answer to a calibration.
     * Calls 'PUT /populations/answers/:worker/:id' from the Object-Service.
     * @param option the option chosen
     * @param worker the worker answered
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitCalibration(int option, int worker) {
        //TODO
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
        //TODO
        return null;
    }

    private CompletableFuture<HttpResponse<JsonNode>> putRequest(String route, UnirestFunction func) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestWithBody request = Unirest.put(url + route)
                        .basicAuth(username, password);
                return func.apply(request);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FunctionalInterface
    private interface UnirestFunction {
        HttpResponse<JsonNode> apply(HttpRequestWithBody requestWithBody) throws UnirestException;
    }
}
