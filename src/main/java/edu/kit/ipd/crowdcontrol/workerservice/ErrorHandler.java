package edu.kit.ipd.crowdcontrol.workerservice;

import edu.kit.ipd.crowdcontrol.workerservice.proto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.error.ServerErrorHandler;
import ratpack.handling.Context;

/**
 * @author LeanderK
 * @version 1.0
 */
public class ErrorHandler implements ServerErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * Processes the given exception that occurred processing the given context.
     * <p>
     * Implementations should strive to avoid throwing exceptions.
     * If exceptions are thrown, they will just be logged at a warning level and the response will be finalised with a 500 error code and empty body.
     *
     * @param context   The context being processed
     * @param throwable The throwable that occurred
     * @throws Exception if something goes wrong handling the error
     */
    @Override
    public void error(Context context, Throwable throwable) throws Exception {
        if (throwable instanceof NotAcceptableException) {
            // Don't use error(...) in this handler,
            // otherwise we end up throwing the same exception again.
            logger.debug("not acceptable request", throwable);
            context.getResponse().status(406);
            context.getResponse().contentType("text/plain");
            context.render("notAcceptable: " + throwable.getMessage());
        } else if (throwable instanceof BadRequestException) {
            logger.debug("bad request", throwable);
            context.getResponse().status(400);
            context.render(error("badRequest", throwable.getMessage()));
        } else if (throwable instanceof InternalServerErrorException) {
            InternalServerErrorException internalError = (InternalServerErrorException) throwable;
            logger.error("an internal error occurred", internalError);
            context.getResponse().status(500);
            context.render(error("internalServerError", internalError.getMessage()));
        } else {
            logger.error("an internal error occurred", throwable);
            context.getResponse().status(500);
            context.render(error("internalServerError", throwable.getMessage()));
        }
    }

    /**
     * Creates an error response-
     *
     * @param code Short error code to make errors machine readable
     * @param detail Detailed error message for humans
     *
     * @return Encoded message
     */
    private ErrorResponse error( String code, String detail) {
        return ErrorResponse.newBuilder().setCode(code).setDetail(detail).build();
    }
}
