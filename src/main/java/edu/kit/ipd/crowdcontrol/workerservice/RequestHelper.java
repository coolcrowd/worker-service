package edu.kit.ipd.crowdcontrol.workerservice;

import spark.Request;

import java.util.function.Predicate;

/**
 * this interface contains various methods to help to deal with SparkJava-Requests.
 * @author LeanderK
 * @version 1.0
 */
public interface RequestHelper {
    /**
     * if the predicate ist false, this method will throw an BadRequestException with the provided message as a cause.
     * @param request the request to check
     * @param predicate the predicate to match against
     * @param message the message describing the tested predicate
     * @throws BadRequestException if the predicate returned false
     */
    default void assertRequest(Request request, Predicate<Request> predicate, String message) throws BadRequestException {
        if (!predicate.test(request)) {
            throw new BadRequestException(message);
        }
    }

    /**
     * asserts that the content-type of the request is 'application/json'.
     * @param request the request to check against
     * @throws BadRequestException if the request has the incorrect content-type
     */
    default void assertJson(Request request) throws BadRequestException {
        String actualContentType = request.contentType();
        assertRequest(request, request1 -> actualContentType != null && actualContentType.equals("application/json"),
                "Content-Type must be 'application/json'!");
    }

    /**
     * asserts that the parameter exists and returns it
     * @param request the request to check against
     * @param parameter the parameter to get
     * @return the value of the parameter
     * @throws BadRequestException if the parameter is not existing
     */
    default String assertParameter(Request request, String parameter) throws BadRequestException {
        assertRequest(request, request1 -> request1.params(parameter) != null, "Request needs Parameter:" + parameter);
        return request.params(parameter);
    }

    /**
     * asserts that the parameter exists and is an integer
     * @param request the request to check against
     * @param parameter the parameter to get
     * @return the value as an integer
     * @throws BadRequestException if the parameter is not existing or is not an int
     */
    default int assertParameterInt(Request request, String parameter) throws BadRequestException {
        assertRequest(request, request1 -> request1.params(parameter) != null, "Request needs Parameter:" + parameter);
        try {
            return Integer.parseInt(request.params(parameter));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Request needs Parameter: " + parameter + " as an Integer");
        }
    }

    /**
     * checks that an query-parameter exists and returns it
     * @param request the request to check
     * @param parameter the query-parameter
     * @return the value of the parameter
     * @throws BadRequestException if the query-parameter is not present
     */
    default String assertQuery(Request request, String parameter) throws BadRequestException {
        assertRequest(request, request1 -> request1.queryMap(parameter) != null, "Request needs Query-Parameter:" + parameter);
        return request.params(parameter);
    }

    /**
     * checks that an query-parameter exists and is an integer
     * @param request the request to check
     * @param parameter the query-parameter
     * @return the value of the parameter as an int
     * @throws BadRequestException if the query parameter is not present or is not an int
     */
    default int assertQueryInt(Request request, String parameter) throws BadRequestException {
        assertRequest(request, request1 -> request1.queryMap(parameter) != null, "Request needs Query-Parameter:" + parameter);
        try {
            return Integer.parseInt(request.params(parameter));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Request needs Query-Parameter: " + parameter + " as an Integer");
        }
    }
}
