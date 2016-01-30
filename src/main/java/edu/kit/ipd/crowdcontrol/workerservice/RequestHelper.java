package edu.kit.ipd.crowdcontrol.workerservice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import spark.Request;
import spark.Response;
import spark.utils.MimeParse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * this interface contains various convenience-methods to help to deal with SparkJava-Requests.
 * @author LeanderK
 * @version 1.0
 */
public interface RequestHelper {
    //TODO: wait till java 9 or expose printer?
    class Const {
        private static JsonFormat.Printer PRINTER = JsonFormat.printer();
    }

    String TYPE_JSON = "application/json";
    String TYPE_PROTOBUF = "application/protobuf";
    List<String> SUPPORTED_TYPES = Collections.unmodifiableList(Arrays.asList(
            "application/protobuf",
            "application/json"
    ));

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
        assertRequest(request, request1 -> request1.queryParamsValues(parameter).length != 0, "Request needs Query-Parameter:" + parameter);
        return request.queryParamsValues(parameter)[0];
    }

    /**
     * checks that an query-parameter exists and is an integer
     * @param request the request to check
     * @param parameter the query-parameter
     * @return the value of the parameter as an int
     * @throws BadRequestException if the query parameter is not present or is not an int
     */
    default int assertQueryInt(Request request, String parameter) throws BadRequestException {
        assertRequest(request, request1 -> request1.queryParamsValues(parameter).length != 0, "Request needs Query-Parameter:" + parameter);
        try {
            return Integer.parseInt(request.queryParamsValues(parameter)[0]);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Request needs Query-Parameter: " + parameter + " as an Integer");
        }
    }

    /**
     * Transforms a protocol buffer object into an JSON / protocol buffer response based on the
     * accept header of the request.
     *
     * @param request
     *         Request provided by Spark.
     * @param response
     *         Response provided by Spark.
     * @param message
     *         Protocol buffer to transform.
     */
   default String transform(Request request, Response response, Message message) {
        String bestMatch = MimeParse.bestMatch(SUPPORTED_TYPES, request.headers("accept"));

        try {
            switch (bestMatch) {
                case TYPE_JSON:
                    response.type(TYPE_JSON);
                    return Const.PRINTER.print(message);
                case TYPE_PROTOBUF:
                    response.type(TYPE_PROTOBUF);
                    return new String(message.toByteArray());
                default:
                    throw new NotAcceptableException(request.headers("accept"), TYPE_JSON, TYPE_PROTOBUF);
            }
        } catch (InvalidProtocolBufferException e) {
            // Can't happen, because we don't use any "Any" fields.
            // https://developers.google.com/protocol-buffers/docs/proto3#any
            throw new InternalServerErrorException("Attempt to transform an invalid protocol buffer into JSON.");
        }
   }
}
