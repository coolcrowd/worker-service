package edu.kit.ipd.crowdcontrol.workerservice.objectservice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import edu.kit.ipd.crowdcontrol.objectservice.proto.Answer;
import edu.kit.ipd.crowdcontrol.objectservice.proto.Rating;
import edu.kit.ipd.crowdcontrol.objectservice.proto.Worker;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;

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
    private final JsonFormat.Parser parser = JsonFormat.parser();
    private final JsonFormat.Printer printer = JsonFormat.printer();
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
                .body(printer.print(worker))
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
                .body(printer.print(answerProto))
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
                .body(printer.print(ratingProto))
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
    public CompletableFuture<HttpResponse<JsonNode>> submitCalibration(int option, int worker) {
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

    /**
     * creates a put-request with the specified route and the function applied
     * @param route the route of the put-request
     * @param func the function to complete the prepared put-request
     * @return an CompletableFuture containing the result of the put-request
     */
    private CompletableFuture<HttpResponse<JsonNode>> putRequest(String route, UnirestFunction func) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestWithBody request = Unirest.put(url + route)
                        .basicAuth(username, password);
                return func.apply(request);
            } catch (UnirestException e) {
                throw new InternalServerErrorException("an error occurred while trying to communicate with the object-service", e);
            } catch (InvalidProtocolBufferException e) {
                throw new InternalServerErrorException("unable to print JSON for request", e);
            }
        });
    }

    /**
     * defines the function to complete the prepared put-request.
     */
    @FunctionalInterface
    private interface UnirestFunction {
        HttpResponse<JsonNode> apply(HttpRequestWithBody requestWithBody) throws UnirestException, InvalidProtocolBufferException;
    }
}
