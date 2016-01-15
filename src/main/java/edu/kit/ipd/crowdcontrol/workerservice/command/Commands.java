package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.RequestHelper;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Answer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Calibration;
import edu.kit.ipd.crowdcontrol.workerservice.proto.EmailAnswer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Rating;
import org.apache.commons.validator.routines.EmailValidator;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

/**
 * The class commands is responsible for the command-part of the CQRS-pattern. Therefore it provides the submit methods matching the available REST-POST commands.
 * @author LeanderK
 * @version 1.0
 */
public class Commands implements RequestHelper {
    private final Communication communication;
    private final JsonFormat.Printer printer = JsonFormat.printer();
    private final JsonFormat.Parser parser = JsonFormat.parser();
    private final ExperimentOperations experimentOperations;

    /**
     * creates an instance of Commands
     * @param communication the communication used to communicate with the object-service
     * @param experimentOperations the experiment-operations used to communicate with the database
     */
    public Commands(Communication communication, ExperimentOperations experimentOperations) {
        this.communication = communication;
        this.experimentOperations = experimentOperations;
    }

    /**
     * The POST-command /email, where this methods corresponds to, persists the email for the specified worker or
     * create a new if none specified.
     * this method expects the JSON-Representation of the protobuf-message email in the body of the request.
     * the response is the corresponding workerID for the email.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return an the JSON-Representation of of the EmailAnswer protobuf-message
     */
    public String submitEmail(Request request, Response response) {
        String platform = assertParameter(request, "platform");
        String email = request.body();
        if (!EmailValidator.getInstance(false).isValid(email)) {
            throw new BadRequestException("invalid email: " + email);
        }
        return communication.submitWorker(email, platform)
                .thenApply(workerID -> EmailAnswer.newBuilder().setWorkerId(workerID).build())
                .thenApply(emailAnswer -> {
                    try {
                        return printer.print(emailAnswer);
                    } catch (InvalidProtocolBufferException e) {
                        throw new InternalServerErrorException("unable to print emailAnswer", e);
                    }
                })
                .handle((emailAnswer, throwable) -> wrapExceptionOr201(emailAnswer, throwable, response))
                .join();
    }

    /**
     * The POST-command /calibration, where this method corresponds to, persists the answer of an worker to the calibration querstions.
     * this method expects the JSON-Representation of the protobuf-message calibration.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return empty body (null)
     */
    public Object submitCalibration(Request request, Response response) {
        doSubmit(request, response, Calibration.newBuilder(), (calibration, workerID) ->
                communication.submitCalibration(calibration.getAnswerOption(), workerID));
        return null;
    }

    /**
     * The POST-command /answer, where this method corresponds to, persists the answer of an worker to a creative-task.
     * this method expects the JSON-Representation of the protobuf-message answer.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return empty body (null)
     */
    public Object submitAnswer(Request request, Response response) {
        doSubmit(request, response, Answer.newBuilder(), (answer, workerID) -> {
            String answerType = experimentOperations.getExperiment(answer.getTask()).getAnswerType();
            try {
                //TODO try this out
                if (answerType != null && !getContentType(answer.getAnswer()).startsWith(answerType)) {
                    throw new BadRequestException("the content-type of the URL does not match the desired type: " + answerType);
                }
            } catch (IOException e) {
                throw new InternalServerErrorException("unable to probe Content-type, aborting", e);
            }
            return communication.submitAnswer(answer.getAnswer(), answer.getTask(), workerID);
        });
        return null;
    }

    /**
     * The POST-command /rating, where this method corresponds to, persists the rating of an answer.
     * this method expects the JSON-Representation of the protobuf-message rating.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return empty body (null)
     */
    public Object submitRating(Request request, Response response) {
        doSubmit(request, response, Rating.newBuilder(), (rating, workerID) ->
                communication.submitRating(rating.getRating(), rating.getTask(), rating.getAnswerId(), workerID));
        return null;
    }

    /**
     * if the throwable is not null it wraps the throwable into an InternalServerErrorException, or else it sets the
     * response to 201 Created.
     * @param t the Data to pass
     * @param throwable the throwable to wrap
     * @param response the response to set 201 to
     * @param <T> the type of the data
     * @return maybe the data (if the throwable is not null
     */
    private <T> T wrapExceptionOr201(T t, Throwable throwable, Response response) {
        if (throwable != null) {
            throw new InternalServerErrorException("an error occurred while communicating with the Object-Service", throwable);
        } else {
            response.status(201);
            return t;
        }
    }

    /**
     * merges the requests body with the Builder
     * @param request the request with the JSON Representation of the Message as a Body
     * @param t the builder
     * @param checkFields whether to check if all fields have been set
     * @param <T> the type of the builder
     * @return the builder with everything set
     * @throws BadRequestException if an error occurred while parsing the JSON or wrong content-type
     */
    private <T extends Message.Builder> T merge(Request request, T t, boolean checkFields) throws BadRequestException {
        assertJson(request);
        try {
            parser.merge(request.body(), t);
        } catch (InvalidProtocolBufferException e) {
            throw new BadRequestException("error while parsing JSON", e);
        }
        if (checkFields) {
            t.getDescriptorForType().getFields().stream()
                    .filter(field -> !t.hasField(field))
                    .findAny()
                    .ifPresent(field -> {
                        throw new BadRequestException("field must be set:" + field.getFullName());
                    });
        }
        return t;
    }

    /**
     * the abstraction for the usual submit.
     * merges the body of the request with the builder, checks the fields and then calls the function. After calling the function it wraps
     * the eventual exception, sets the status accordingly and blocks for return.
     * @param request the request
     * @param response the response
     * @param x the builder for the expected message
     * @param func the function to execute
     * @param <X> the type of the builder
     * @param <T> the type of the return data
     * @return an instance of T or an exception
     */
    private <X extends Message.Builder, T> T doSubmit(Request request, Response response, X x, BiFunction<X, Integer,  CompletableFuture<T>> func) {
        int workerID = assertParameterInt(request, "workerID");
        X builder = merge(request, x, true);
        return func.apply(builder, workerID)
                .handle((t, throwable) -> wrapExceptionOr201(t, throwable, response))
                .join();
    }

    /**
     * Http HEAD Method to get URL content type
     *
     * @param urlString
     * @return content type
     * @throws IOException
     */
    //from: http://stackoverflow.com/questions/5801993/quickest-way-to-get-content-type
    public static String getContentType(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        if (isRedirect(connection.getResponseCode())) {
            String newUrl = connection.getHeaderField("Location"); // get redirect url from "location" header field
            return getContentType(newUrl);
        }
        return connection.getContentType();
    }

    /**
     * Check status code for redirects
     *
     * @param statusCode
     * @return true if matched redirect group
     */
    //from: http://stackoverflow.com/questions/5801993/quickest-way-to-get-content-type
    protected static boolean isRedirect(int statusCode) {
        if (statusCode != HttpURLConnection.HTTP_OK) {
            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                    || statusCode == HttpURLConnection.HTTP_SEE_OTHER) {
                return true;
            }
        }
        return false;
    }
}
