package edu.kit.ipd.crowdcontrol.workerservice;

import edu.kit.ipd.crowdcontrol.workerservice.command.Commands;
import edu.kit.ipd.crowdcontrol.workerservice.proto.ErrorResponse;
import edu.kit.ipd.crowdcontrol.workerservice.query.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.servlet.SparkApplication;

import javax.servlet.AsyncContext;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiFunction;

import static spark.Spark.*;

/**
 * the router is the glue that connects the methods from the Command & Query package to the REST-interfaces.
 * @author LeanderK
 * @version 1.0
 */
public class Router implements SparkApplication, RequestHelper {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);
    private final Queries queries;
    private final Commands commands;
    private final int port;

    /**
     * creates a new Router.
     * @param queries the query to call
     * @param commands the commands to call
     * @param port the port the server is listening on
     */
    public Router(Queries queries, Commands commands, int port) {
        this.queries = queries;
        this.commands = commands;
        this.port = port;
    }

    /**
     * Invoked from the SparkFilter. Used to define routes.
     */
    @Override
    public void init() {
        port(port);

        exception(Exception.class, (exception, request, response) -> {
            logger.error("an internal error occurred", exception);
            response.status(501);
            response.body(error(request, response, "internalServerError", exception.getMessage()));
        });

        exception(BadRequestException.class, (exception, request, response) -> {
            logger.debug("bad request", exception);
            response.status(400);
            response.body(error(request, response, "badRequest", exception.getMessage()));
        });

        exception(InternalServerErrorException.class, (exception, request, response) -> {
            InternalServerErrorException internalError = (InternalServerErrorException) exception;
            logger.error("an internal error occurred", internalError);
            response.status(500);
            response.body(error(request, response, "internalServerError", exception.getMessage()));
        });

        exception(NotAcceptableException.class, (exception, request, response) -> {
            // Don't use error(...) in this handler,
            // otherwise we end up throwing the same exception again.
            logger.debug("not acceptable request", exception);
            response.status(406);
            response.type("text/plain");
            response.body("notAcceptable: " + exception.getMessage());
        });

        before((request, response) -> {
            response.header("access-control-allow-origin", "*");
            response.header("access-control-allow-methods", "GET,PUT,POST,PATCH,DELETE,OPTIONS");
            response.header("access-control-allow-credentials", "true");
            response.header("access-control-allow-headers", "Authorization,Content-Type");
            response.header("access-control-expose-headers", "Link,Location");
            response.header("access-control-max-age", "86400");
        });

        get("/preview/:experiment", concurrentUnwrap(queries::preview));

        get("/next/:platform/:experiment", concurrentUnwrap(queries::getNext));

        post("/emails/:platform", concurrentUnwrap(commands::submitEmail));
        options("/emails/:workerID", (request, response) -> {
            response.status(200);
            return "";
        });

        post("/answers/:workerID", concurrentUnwrap(commands::submitAnswer));
        options("/answers/:workerID", (request, response) -> {
            response.status(200);
            return "";
        });

        post("/ratings/:workerID", concurrentUnwrap(commands::submitRating));
        options("/ratings/:workerID", (request, response) -> {
            response.status(200);
            return "";
        });

        post("/calibrations/:workerID", concurrentUnwrap(commands::submitCalibration));
        options("/calibrations/:workerID", (request, response) -> {
            response.status(200);
            return "";
        });

    }

    /**
     * Creates an error response and encodes it into JSON / protocol buffers.
     *
     * @param request Request provided by Spark
     * @param response Response provided by Spark
     * @param code Short error code to make errors machine readable
     * @param detail Detailed error message for humans
     *
     * @return Encoded message
     */
    private String error(Request request, Response response, String code, String detail) {
        ErrorResponse error = ErrorResponse.newBuilder().setCode(code).setDetail(detail).build();
        return transform(request, response, error);
    }

    /**
     * catches CompletionException and handles accordingly
     * @param route the route to map
     * @return a safeguarded route
     */
    private Route concurrentUnwrap(Route route) {
        return (request, response) -> {
            try {
                return route.handle(request, response);
            } catch (CompletionException exception) {
                if (exception.getCause() != null && exception.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) exception.getCause();
                }
                throw new InternalServerErrorException(exception.getMessage(), exception);
            }
        };
    }
}
