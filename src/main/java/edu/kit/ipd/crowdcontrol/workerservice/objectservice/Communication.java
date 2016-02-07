package edu.kit.ipd.crowdcontrol.workerservice.objectservice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.options.Option;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import edu.kit.ipd.crowdcontrol.objectservice.proto.*;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.NotAcceptableException;

import java.lang.Integer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * this class is used to communicate with the object-Service.
 * @author LeanderK
 * @version 1.0
 */
public class Communication {
    private final String url;
    private final JsonFormat.Printer printer = JsonFormat.printer();
    private final JsonFormat.Parser parser = JsonFormat.parser();
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
    public CompletableFuture<Integer> submitWorker(String email, String platform, Map<String, String[]> originalQueryParameter) {
        HashMap<String, String[]> queryParameter = new HashMap<>(originalQueryParameter);
        queryParameter.put("email", new String[]{email});

        return tryGetWorkerID(platform, queryParameter)
                .thenApply(optional -> {
                    if (optional.isPresent()) {
                        return optional.get();
                    } else {
                        throw new InternalServerErrorException("unable to get new worker!");
                    }
                })
                .thenApply(workerID -> Worker.newBuilder().setId(workerID).setPlatform(platform).setEmail(email).build())
                .thenCompose(worker ->
                        patchRequest("/workers/"+worker.getId(), builder -> builder
                        .body(printer.print(worker))
                        .asJson())
                )
                .thenApply(response -> {
                    if (response.getStatus() == 200) {
                        return response.getBody().getObject().getInt("id");
                    } else if (response.getStatus() == 409) {
                        return Integer.parseInt(response.getHeaders().getFirst("Location").replace(url+"/workers/", ""));
                    } else {
                        return throwOr(response, () -> {throw new RuntimeException("illegal Response, status : " + response.getStatus());});
                    }
                });
    }

    /**
     * submits an answer for the worker
     * Calls 'PUT /populations/answers' from the Object-Service.
     * @param answer the answer to submit
     * @param experiment the experiment working on
     * @param worker the worker answered
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Integer> submitAnswer(String answer, int experiment, int worker) {
        Answer answerProto = Answer.newBuilder()
                .setContent(answer)
                .setWorker(worker)
                .setExperimentId(experiment)
                .build();
        return putRequest("/experiments/" + experiment + "/answers", builder -> builder
                .body(printer.print(answerProto))
                .asJson()
        ).thenApply(response -> {
            if (response.getStatus() == 201) {
                return response.getBody().getObject().getInt("id");
            } else if (response.getStatus() == 409) {
                return Integer.parseInt(
                        response.getHeaders()
                        .getFirst("Location")
                        .replace(url+"/experiments/"+experiment+"/answers/", "")
                );
            } else {
                return throwOr(response, () -> {throw new RuntimeException("illegal Response, status : " + response.getStatus());});
            }
        });
    }

    /**
     * submits an rating for the worker.
     * Calls 'PUT /populations/ratings' from the Object-Service.
     *
     * @param ratingId
     * @param chosenRating the rating to submit
     * @param feedback the feedback or null
     * @param experiment the experiment working on
     * @param answer the rated answer
     * @param worker the worker responsible
     * @param constraints the violated constraints
     * @return an completable future representing the request, with the status the result
     */
    public CompletableFuture<Integer> submitRating(int ratingId, int chosenRating, String feedback, int experiment, int answer, int worker, List<Integer> constraints) {
        List<Constraint> constraintProtos = constraints.stream()
                .map(constraint -> Constraint.newBuilder().setId(constraint).build())
                .collect(Collectors.toList());
        Rating.Builder ratingBuilder = Rating.newBuilder()
                .setRatingId(ratingId)
                .setRating(chosenRating)
                .setWorker(worker)
                .addAllViolatedConstraints(constraintProtos);
        if (feedback != null)
            ratingBuilder = ratingBuilder.setFeedback(feedback);
        Rating rating = ratingBuilder.build();
        return putRequest("/experiments/"+experiment+"/answers/"+answer+"/rating", builder -> builder
                .body(printer.print(rating))
                .asJson()
        ).thenApply(response -> throwOr(response, () -> response))
                .thenApply(HttpResponse::getStatus);
    }

    /**
     * submits an answer to a calibration.
     * Calls 'PUT /populations/answers/:worker/:id' from the Object-Service.
     * @param option the option chosen
     * @param worker the worker answered
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<HttpResponse<JsonNode>> submitCalibration(int option, int worker) {
        CalibrationAnswer calibrationAnswer = CalibrationAnswer.newBuilder()
                .setAnswerId(option)
                .build();

        return putRequest(String.format("/workers/%d/calibrations", worker), builder -> builder
                .body(printer.print(calibrationAnswer))
                .asJson()
        )
        .thenApply(response -> throwOr(response, () -> null));
    }

    /**
     * tries to get the workerID from the request.
     * Calls 'GET /getWorkerID/:platform' from the Object-Service.
     * @param platform the current platform
     * @param queryParameter the passed query-Parameter
     * @return an completable future representing the request with the resulting location in the database
     */
    public CompletableFuture<Optional<Integer>> tryGetWorkerID(String platform, Map<String, String[]> queryParameter) {
        return getRequest("/workers/"+platform+"/identity", builder -> {
            HttpRequest request = builder.getHttpRequest();
            for (Map.Entry<String, String[]> entry : queryParameter.entrySet()) {
                request = request.queryString(entry.getKey(), entry.getValue()[0]);
            }
            return request
                    .asJson();
        }).thenApply(response -> {
            if (response.getStatus() == 200) {
                return Optional.of(response.getBody().getObject().getInt("id"));
            } else if (response.getStatus() == 409) {
                return Optional.of(Integer.parseInt(response.getHeaders().getFirst("Location").replace(url+"/workers/", "")));
            } else if (response.getStatus() == 404){
                return Optional.empty();
            }
            return throwOr(response, Optional::empty);
        });
    }

    /**
     * tries to recover the error detail from the object-service if possible and rethrows with the appropriate exception
     * or returns otherwise
     * @param response the response to react to
     * @param otherwise what to return otherwise
     * @param <T> the Type to return
     * @return the result of the supplier if not an exception
     */
    private <T> T throwOr(HttpResponse<JsonNode> response, Supplier<T> otherwise) {
        int status = response.getStatus();
        Function<String, String> getError = osMessage -> String.format("Cause: Object service responded with: %s", osMessage);
        try {
            if (status == 400) {
                ErrorResponse.Builder errorBuilder = ErrorResponse.newBuilder();
                parser.merge(response.getBody().toString(), errorBuilder);
                throw new BadRequestException(getError.apply(errorBuilder.getDetail()));
            } else if (status == 500) {
                ErrorResponse.Builder errorBuilder = ErrorResponse.newBuilder();
                parser.merge(response.getBody().toString(), errorBuilder);
                throw new InternalServerErrorException(getError.apply(errorBuilder.getDetail()));
            } else if (status == 406) {
                throw new NotAcceptableException(getError.apply(response.getBody().toString()));
            } else {
                return otherwise.get();
            }
        } catch (InvalidProtocolBufferException e) {
            throw new InternalServerErrorException(String.format("Object service responded with: %s", response.getBody().toString()));
        }
    }

    /**
     * creates a put-request with the specified route and the function applied
     * @param route the route of the put-request
     * @param func the function to complete the prepared put-request
     * @return an CompletableFuture containing the result of the put-request
     */
    private CompletableFuture<HttpResponse<JsonNode>> putRequest(String route, UnirestFunction<HttpRequestWithBody> func) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestWithBody request = Unirest.put(url + route)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
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
     * creates a put-request with the specified route and the function applied
     * @param route the route of the put-request
     * @param func the function to complete the prepared put-request
     * @return an CompletableFuture containing the result of the put-request
     */
    private CompletableFuture<HttpResponse<JsonNode>> patchRequest(String route, UnirestFunction<HttpRequestWithBody> func) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestWithBody request = Unirest.patch(url + route)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
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
     * creates a put-request with the specified route and the function applied
     * @param route the route of the put-request
     * @param func the function to complete the prepared put-request
     * @return an CompletableFuture containing the result of the put-request
     */
    private CompletableFuture<HttpResponse<JsonNode>> getRequest(String route, UnirestFunction<GetRequest> func) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                GetRequest request = Unirest.get(url + route)
                        .header("accept", "application/json")
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
    private interface UnirestFunction<T> {
        HttpResponse<JsonNode> apply(T request) throws UnirestException, InvalidProtocolBufferException;
    }
}
