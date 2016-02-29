package edu.kit.ipd.crowdcontrol.workerservice.objectservice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import edu.kit.ipd.crowdcontrol.objectservice.proto.*;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.NotAcceptableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Integer;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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
    private static final Logger logger = LoggerFactory.getLogger(Communication.class);

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


        logger.trace("Request to get WorkerID for email {}.", email);
        return tryGetWorkerID(platform, queryParameter)
                .thenApply(optional -> {
                    if (optional.isPresent()) {
                        return optional.get();
                    } else {
                        throw new InternalServerErrorException("unable to get new worker!");
                    }
                })
                .thenApply(workerID -> Worker.newBuilder().setId(workerID).setPlatform(platform).setEmail(email).build())
                .thenCompose(worker -> {
                    String route = "/workers/" + worker.getId();
                    logger.debug("Request to patch worker {} with new email {} for route {}.", worker.getId(),
                            worker.getEmail(), route);
                    return patchRequest(route, builder -> builder
                            .body(printer.print(worker)));
                })
                .thenApply(response -> {
                    if (response.getStatus() == 200) {
                        logger.debug("Object service answered with status 200");
                        return response.getBody().getObject().getInt("id");
                    } else if (response.getStatus() == 409) {
                        logger.debug("Object service answered with status 409");
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
        String route = "/experiments/" + experiment + "/answers";
        logger.debug("Trying to submit answer {} for worker {} with route {}", answer, worker, route);
        return putRequest(route, builder -> builder
                .body(printer.print(answerProto))
        ).thenApply(response -> {
            if (response.getStatus() == 201) {
                logger.debug("Object service answered with status 201");
                return response.getBody().getObject().getInt("id");
            } else if (response.getStatus() == 409) {
                logger.debug("Object service answered with status 409");
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
                .setExperimentId(experiment)
                .addAllViolatedConstraints(constraintProtos);
        if (feedback != null)
            ratingBuilder = ratingBuilder.setFeedback(feedback);
        Rating rating = ratingBuilder.build();
        String route = "/experiments/" + experiment + "/answers/" + answer + "/rating";
        logger.debug("Trying to submit rating {} for worker {} with route {}", rating, worker, route);
        return putRequest(route, builder -> builder
                .body(printer.print(rating))
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

        String route = String.format("/workers/%d/calibrations", worker);
        logger.debug("Trying to submit calibration {} for worker {} with route {}", calibrationAnswer, worker, route);
        return putRequest(route, builder -> builder
                .body(printer.print(calibrationAnswer))
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
        String route = "/workers/" + platform + "/identity";
        logger.debug("Trying to get workerId for parameter {} with route {} from platform {}.", queryParameter, route, platform);
        return getRequest(route, builder -> {
            HttpRequest request = builder.getHttpRequest();
            for (Map.Entry<String, String[]> entry : queryParameter.entrySet()) {
                request = request.queryString(entry.getKey(), entry.getValue()[0]);
            }
            return request;
        }).thenApply(response -> {
            if (response.getStatus() == 200) {
                logger.debug("Object service answered with status 200");
                return Optional.of(response.getBody().getObject().getInt("id"));
            } else if (response.getStatus() == 409) {
                logger.debug("Object service answered with status 409");
                return Optional.of(Integer.parseInt(response.getHeaders().getFirst("Location").replace(url+"/workers/", "")));
            } else if (response.getStatus() == 404){
                logger.debug("Object service answered with status 404");
                return Optional.empty();
            }
            return throwOr(response, Optional::empty);
        });
    }

    /**
     * this method calls 'GET /algorithms' and checks that the result is not empty.
     * @return true if the ObjectService is running
     */
    public boolean isObjectServiceRunning() {
        String route = "/algorithms";
        try {
            return getRequest(route, builder -> builder)
                    //not very sophisticated yet
                    .thenApply(json -> true)
                    .join();
        } catch (CompletionException e) {
            logger.debug("object-service is not yet available");
            logger.trace("an exception occurred while trying to communicate with the os", e);
            return false;
        }
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
                logger.debug("Object service answered with status {}", status);
                return otherwise.get();
            }
        } catch (InvalidProtocolBufferException e) {
            throw new InternalServerErrorException(String.format("Object service responded with: %s", response.getBody().toString()));
        }
    }

    /**
     * non-blocking async operation
     * @param request the request to use
     * @param func the function to process the request
     * @param <T> the type of the request
     * @return a completableFuture completed when the request is finished
     */
    public <T extends HttpRequest> CompletableFuture<HttpResponse<JsonNode>> doAsync(T request, UnirestFunction<T> func) {
        CompletableFuture<HttpResponse<JsonNode>> future = new CompletableFuture<>();
        Callback<JsonNode> callback = new Callback<JsonNode>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                future.complete(response);
            }

            @Override
            public void failed(UnirestException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void cancelled() {
                future.cancel(true);
            }
        };
        try {
            func.apply(request)
                    .asJsonAsync(callback);
        } catch (UnirestException e) {
            future.completeExceptionally(new InternalServerErrorException("an error occurred while trying to communicate with the object-service", e));
        } catch (InvalidProtocolBufferException e) {
            future.completeExceptionally(new InternalServerErrorException("unable to print JSON for request", e));
        } catch (RuntimeException e) {
            if (e.getCause() != null && e.getCause() instanceof URISyntaxException)  {
                future.completeExceptionally(e);
            } else {
                throw e;
            }
        }
        return future;
    }

    /**
     * creates a put-request with the specified route and the function applied
     * @param route the route of the put-request
     * @param func the function to complete the prepared put-request
     * @return an CompletableFuture containing the result of the put-request
     */
    private CompletableFuture<HttpResponse<JsonNode>> putRequest(String route, UnirestFunction<HttpRequestWithBody> func) {
        return doAsync(Unirest.put(url + route)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .basicAuth(username, password),
                func);
    }

    /**
     * creates a put-request with the specified route and the function applied
     * @param route the route of the put-request
     * @param func the function to complete the prepared put-request
     * @return an CompletableFuture containing the result of the put-request
     */
    private CompletableFuture<HttpResponse<JsonNode>> patchRequest(String route, UnirestFunction<HttpRequestWithBody> func) {
        return doAsync(Unirest.patch(url + route)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .basicAuth(username, password),
                func);
    }

    /**
     * creates a put-request with the specified route and the function applied
     * @param route the route of the put-request
     * @param func the function to complete the prepared put-request
     * @return an CompletableFuture containing the result of the put-request
     */
    private CompletableFuture<HttpResponse<JsonNode>> getRequest(String route, UnirestFunction<GetRequest> func) {
        return doAsync(Unirest.get(url + route)
                        .header("accept", "application/json")
                        .basicAuth(username, password),
                func);
    }

    /**
     * defines the function to complete the prepared put-request.
     */
    @FunctionalInterface
    private interface UnirestFunction<T extends HttpRequest> {
        BaseRequest apply(T request) throws UnirestException, InvalidProtocolBufferException;
    }
}
