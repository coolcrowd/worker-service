package edu.kit.ipd.crowdcontrol.workerservice;

import edu.kit.ipd.crowdcontrol.workerservice.command.Commands;
import edu.kit.ipd.crowdcontrol.workerservice.query.Query;
import spark.servlet.SparkApplication;

import static spark.Spark.*;

/**
 * the router is the glue that connects the methods from the Command & Query package to the REST-interfaces.
 * @author LeanderK
 * @version 1.0
 */
public class Router implements SparkApplication {
    private final Query query;
    private final Commands commands;

    /**
     * creates a new Router.
     * @param query the query to call
     * @param commands the commands to call
     */
    public Router(Query query, Commands commands) {
        this.query = query;
        this.commands = commands;
    }

    /**
     * Invoked from the SparkFilter. Used to define routes.
     */
    @Override
    public void init() {
        exception(BadRequestException.class, (exception, request, response) -> {
            BadRequestException badRequest = (BadRequestException) exception;
            response.status(400);
            response.body(badRequest.getMessage());
        });

        exception(InternalServerErrorException.class, (exception, request, response) -> {
            InternalServerErrorException internalError = (InternalServerErrorException) exception;
            response.status(500);
            response.body(internalError.getMessage());
            System.err.println("an internal error occurred");
            internalError.printStackTrace();
        });

        get("/next/:platform/:experiment", query::getNext);

        post("/email/:platform", commands::submitEmail);

        post("/answer/:workerID", commands::submitAnswer);

        post("/rating/:workerID", commands::submitRating);

        post("/calibration/:workerID", commands::submitCalibration);

    }
}
