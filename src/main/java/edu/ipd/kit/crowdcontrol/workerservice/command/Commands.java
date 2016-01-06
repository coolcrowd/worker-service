package edu.ipd.kit.crowdcontrol.workerservice.command;

import edu.ipd.kit.crowdcontrol.workerservice.RequestHelper;
import edu.ipd.kit.crowdcontrol.workerservice.objectservice.Communication;
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
     *
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return
     */
    public Object submitEmail(Request request, Response response) {
        return null;
    }

    /**
     *
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return
     */
    public Object submitCalibration(Request request, Response response) {
        return null;
    }

    /**
     *
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return
     */
    public Object submitAnswer(Request request, Response response) {
        return null;
    }

    /**
     *
     * @param request  The request providing information about the HTTP request
     * @param response The response providing functionality for modifying the response
     * @return
     */
    public Object submitRating(Request request, Response response) {
        return null;
    }
}
