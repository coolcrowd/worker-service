package edu.ipd.kit.crowdcontrol.workerservice;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import edu.ipd.kit.crowdcontrol.workerservice.command.Commands;
import edu.ipd.kit.crowdcontrol.workerservice.query.Query;
import spark.Request;
import spark.servlet.SparkApplication;

import java.util.function.Function;

import static spark.Spark.*;

/**
 * the router is the glue that connects the methods from the Command & Query package to the REST-interfaces.
 * @author LeanderK
 * @version 1.0
 */
public class Router implements SparkApplication {
    private final Query query;
    private final Commands commands;
    private final JsonFormat protobufJSON = new JsonFormat();

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
            BadRequestException exception1 = (BadRequestException) exception;
            //TODO!
        });

        get("/next/:platform/:experiment", query::getNext);

        post("/email/:platform", commands::submitEmail);

        post("/answer/:worker", commands::submitAnswer);

        post("/rating/:worker", commands::submitRating);

        post("/calibration/:worker", commands::submitCalibration);
    }

    private void get(String route, Function<Request, Message> handler) {
        //TODO: do response things
        spark.Spark.get(route, (request, response) -> protobufJSON.printToString(handler.apply(request)));
    }
}
