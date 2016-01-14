package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.RequestHelper;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Answer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Calibration;
import edu.kit.ipd.crowdcontrol.workerservice.proto.EmailAnswer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Rating;
import org.apache.commons.validator.routines.EmailValidator;
import spark.Request;
import spark.Response;

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

    /**
     * creates an instance of Commands
     * @param communication the communication used to communicate with the object-service
     */
    public Commands(Communication communication) {
        this.communication = communication;
    }

    /**
     * The POST-command /email, where this methods corresponds to, persists the email for the specified worker or
     * create a new if none specified.
     * this method expects the JSON-Representation of the protobuf-message email in the body of the request.
     * the response is the corresponding workerID for the email.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return an instance of EmailAnswer
     */
    public EmailAnswer submitEmail(Request request, Response response) {
        String platform = assertParameter(request, ":platform");
        String email = request.body();
        if (!EmailValidator.getInstance(false).isValid(email)) {
            throw new BadRequestException("invalid email: " + email);
        }
        return communication.putWorker(email, platform)
                .thenApply(workerID -> EmailAnswer.newBuilder().setWorkerId(workerID).build())
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
            //TODO: wrap in right exception!
            throw new InternalServerErrorException("an error occurred while communicating with the Object-Service", throwable);
        } else {
            response.status(200);
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
}
