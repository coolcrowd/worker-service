package edu.kit.ipd.crowdcontrol.workerservice.command;

import edu.kit.ipd.crowdcontrol.workerservice.RequestHelper;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Emailanswer;
import spark.Request;
import spark.Response;

/**
 * The class commands is responsible for the command-part of the CQRS-pattern. Therefore it provides the submit methods matching the available REST-POST commands.
 * @author LeanderK
 * @version 1.0
 */
public class Commands implements RequestHelper {
    private final Communication communication;

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
     * @return an instance of Emailanswer
     */
    public Emailanswer submitEmail(Request request, Response response) {
        return null;
    }

    /**
     * The POST-command /calibration, where this method corresponds to, persists the answer of an worker to the calibration querstions.
     * this method expects the JSON-Representation of the protobuf-message calibration.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return emtpy body (null)
     */
    public Object submitCalibration(Request request, Response response) {
        return null;
    }

    /**
     * The POST-command /answer, where this method corresponds to, persists the answer of an worker to a creative-task.
     * this method expects the JSON-Representation of the protobuf-message answer.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return emtpy body (null)
     */
    public Object submitAnswer(Request request, Response response) {
        return null;
    }

    /**
     * The POST-command /rating, where this method corresponds to, persists the rating of an answer.
     * this method expects the JSON-Representation of the protobuf-message rating.
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return emtpy body (null)
     */
    public Object submitRating(Request request, Response response) {
        return null;
    }
}
