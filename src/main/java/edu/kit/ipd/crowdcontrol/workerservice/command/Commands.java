package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
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
    private final JsonFormat protobufJSON = new JsonFormat();
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
        String platform = assertParameter(request, ":platform");
        String email = request.body();
        if (!EmailValidator.getInstance(false).isValid(email)) {
            throw new BadRequestException("invalid email: " + email);
        }
        return communication.submitWorker(email, platform)
                .thenApply(workerID -> EmailAnswer.newBuilder().setWorkerId(workerID).build())
                .thenApply(protobufJSON::printToString)
                .handle((emailAnswer, throwable) -> wrapException(emailAnswer, throwable, response))
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
                communication.submitCalibration(calibration.getAnswerOption(), workerID)
        );
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
            if (answer.getAnswer() == null || answer.getAnswer().isEmpty()) {
                throw new BadRequestException("this request requires the answer set");
            }
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
                communication.submitRating(rating.getRating(), rating.getTask(), rating.getAnswerId(), workerID)
        );
        return null;
    }

    private <T> T wrapException(T t, Throwable throwable, Response response) {
        if (throwable != null) {
            throw new InternalServerErrorException("an error occurred while communicating with the Object-Service", throwable);
        } else {
            response.status(201);
            return t;
        }
    }

    private <T extends Message.Builder> T merge(Request request, T t) throws BadRequestException {
        try {
            protobufJSON.merge(request.body(), ExtensionRegistry.newInstance() , t);
        } catch (JsonFormat.ParseException e) {
            throw new BadRequestException("error while parsing JSON", e);
        }
        return t;
    }

    private <X extends Message.Builder, T> T doSubmit(Request request, Response response, X x, BiFunction<X, Integer,  CompletableFuture<T>> func) {
        int workerID = assertParameterInt(request, "workerID");
        X builder = merge(request, x);
        return func.apply(builder, workerID)
                .handle((t, throwable) -> wrapException(t, throwable, response))
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
