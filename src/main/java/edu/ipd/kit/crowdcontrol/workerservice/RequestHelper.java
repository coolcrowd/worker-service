package edu.ipd.kit.crowdcontrol.workerservice;

import spark.Request;

import java.util.function.Predicate;

/**
 * contains various methods to help to deal with Requests.
 * @author LeanderK
 * @version 1.0
 */
public interface RequestHelper {
    default void assertRequest(Request request, Predicate<Request> predicate, String message) {
        if (!predicate.test(request)) {
            throw new BadRequestException(message);
        }
    }

    default void assertJson(Request request) {
        String actualContentType = request.headers("Content-Type");
        assertRequest(request, request1 -> actualContentType != null && actualContentType.equals("application/json"),
                "Content-Type must be 'application/json'!");
    }

    default String assertParameter(Request request, String parameter) {
        assertRequest(request, request1 -> request1.params(parameter) != null, "Request needs Parameter:" + parameter);
        return request.params(parameter);
    }

    default int assertParameterInt(Request request, String parameter) {
        assertRequest(request, request1 -> request1.params(parameter) != null, "Request needs Parameter:" + parameter);
        try {
            return Integer.parseInt(request.params(parameter));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Request needs Parameter: " + parameter + " as an Integer");
        }
    }

    default String assertQuery(Request request, String parameter) {
        assertRequest(request, request1 -> request1.queryMap(parameter) != null, "Request needs Query-Parameter:" + parameter);
        return request.params(parameter);
    }

    default int assertQueryInt(Request request, String parameter) {
        assertRequest(request, request1 -> request1.queryMap(parameter) != null, "Request needs Query-Parameter:" + parameter);
        try {
            return Integer.parseInt(request.params(parameter));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Request needs Query-Parameter: " + parameter + " as an Integer");
        }
    }
}
