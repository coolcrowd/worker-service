package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.net.MediaType;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.*;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformNotFoundException;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.*;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Answer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Calibration;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Rating;
import org.apache.commons.validator.routines.EmailValidator;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.http.TypedData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collector;

/**
 * The class commands is responsible for the command-part of the CQRS-pattern. Therefore it provides the submit methods
 * matching the available REST-POST commands.
 *
 * @author LeanderK
 * @version 1.0
 */
public class Commands implements RequestHelper {
    private static final Logger logger = LoggerFactory.getLogger(Commands.class);
    private final Communication communication;
    private final JsonFormat.Parser parser = JsonFormat.parser();
    private final ExperimentOperations experimentOperations;
    private final PlatformOperations platformOperations;
    private final JWTHelper jwtHelper;

    /**
     * creates an instance of Commands
     * @param communication        the communication used to communicate with the object-service
     * @param experimentOperations the experiment-operations used to communicate with the database
     * @param platformOperations the platform-operations to use
     * @param jwtHelper the Helper used to parse/generate the JWT-authentication
     */
    public Commands(Communication communication, ExperimentOperations experimentOperations, PlatformOperations platformOperations, JWTHelper jwtHelper) {
        this.communication = communication;
        this.experimentOperations = experimentOperations;
        this.platformOperations = platformOperations;
        this.jwtHelper = jwtHelper;
    }

    /**
     * The POST-command /email, where this methods corresponds to, persists the email for the specified worker or
     * create a new if none specified.
     * this method expects the JSON-Representation of the protobuf-message email in the body of the request.
     * the response is the corresponding workerID for the email.
     *
     * @param context the Context of the Request
     * @return an the JSON-Representation of of the EmailAnswer protobuf-message
     */
    public Promise<EmailAnswer> submitEmail(Context context) {
        String platform = assertParameter(context, "platform");
        try {
            platformOperations.getPlatform(platform);
        } catch (PlatformNotFoundException e) {
            throw new NotFoundException(String.format("Platform %s not found", platform));
        }
        return merge(context, Email.newBuilder(), new ArrayList<>())
                .map(Email.Builder::build)
                .map(toValidate -> {
                    logger.debug("Request submit to submit email {} for platform {}", toValidate, platform);
                    if (!EmailValidator.getInstance(false).isValid(toValidate.getEmail())) {
                        throw new BadRequestException("invalid email: " + toValidate.getEmail());
                    }
                    return toValidate;
                })
                .flatMap(email -> {
                    LinkedListMultimap<String, String> parameters = email.getPlatformParametersList().stream()
                            .collect(Collector.of(
                                    LinkedListMultimap::<String, String>create,
                                    (map, param) -> map.putAll(param.getKey(), param.getValuesList()),
                                    (map1, map2) -> {
                                        map1.putAll(map2);
                                        return map2;
                                    }
                            ));
                    return Promise.<Integer>of(downstream -> downstream.accept(
                            communication.submitWorker(email.getEmail(), platform, parameters)
                                    .handle((emailAnswer, throwable) -> wrapExceptionOr201(emailAnswer, throwable, context))
                            ))
                            .map(workerID -> EmailAnswer.newBuilder().setAuthorization(jwtHelper.generateJWT(workerID)).build())
                            .map(emailAnswer -> {
                                logger.debug("Answer from Object-Service for submitting email {}: {}", email, emailAnswer.toString());
                                return emailAnswer;
                            });
                });
    }

    /**
     * The POST-command /calibration, where this method corresponds to, persists the answer of an worker to the
     * calibration questions.
     * this method expects the JSON-Representation of the protobuf-message calibration.
     *
     * @param context the Context of the Request
     * @return empty body (null)
     */
    public Promise<String> submitCalibration(Context context) {
        return doSubmit(context, Calibration.newBuilder(), (calibration, workerID) -> {
            logger.debug("Trying to submit calibration {} for worker {}.", calibration, workerID);
            return communication.submitCalibration(calibration.getAnswerOption(), workerID);
        }).map(result -> {
            logger.debug("Object service answered with OK.");
            return "Calibration submitted";
        });
    }

    /**
     * The POST-command /answer, where this method corresponds to, persists the answer of an worker to a creative-task.
     * this method expects the JSON-Representation of the protobuf-message answer.
     *
     * @param context the Context of the Request
     * @return empty body (null)
     */
    public Promise<String> submitAnswer(Context context) {
        return doSubmit(context, Answer.newBuilder(), (answer, workerID) -> {
            logger.debug("Request to submit answer {} for worker {}", answer, workerID);
            String answerType = experimentOperations.getExperiment(answer.getExperiment()).getAnswerType();
            try {
                if (answerType != null) {
                    String mime = getContentType(answer.getAnswer());
                    MediaType expected = MediaType.parse(answerType);
                    MediaType got = MediaType.parse(mime);
                    if (!got.is(expected)) {
                        throw new BadRequestException("the content-type of the URL does not match the desired type: " +
                                answerType);
                    }
                }
            } catch (IOException e) {
                throw new InternalServerErrorException("unable to probe Content-type, aborting", e);
            }
            if (answerType == null) {
                logger.trace("cleaning answer {} for worker {} form potential malicious html tags", answer, workerID);
                answer.setAnswer(Jsoup.clean(answer.getAnswer(), Whitelist.basic()));
            }
            logger.trace("answer {} for reservation {} from worker {} is valid", answer, answer.getReservation(), workerID);
            return communication.submitAnswer(answer.getAnswer(), answer.getReservation(), answer.getExperiment(), workerID);
        }).map(result -> {
            logger.debug("Object service answered with OK.");
            return "Answer submitted";
        });
    }

    /**
     * The POST-command /rating, where this method corresponds to, persists the rating of an answer.
     * this method expects the JSON-Representation of the protobuf-message rating.
     *
     * @param context the Context of the Request
     * @return empty body (null)
     */
    public Promise<String> submitRating(Context context) {
        return doSubmit(context, Rating.newBuilder(),
                //excluded rating because 0 is valid
                Arrays.asList(Rating.FEEDBACK_FIELD_NUMBER, Rating.RATING_FIELD_NUMBER), (rating, workerID) -> {
                    logger.debug("Request to submit rating {} for worker {}.", rating, workerID);
                    if (rating.getConstraintsCount() > 0) {
                        logger.debug("rating {} from worker {} has constraints, setting rating to 0", rating.getRatingId(), workerID);
                        rating.setRating(0);
                    }
                    return communication.submitRating(
                            rating.getRatingId(),
                            rating.getRating(),
                            rating.getFeedback(),
                            rating.getExperiment(),
                            rating.getAnswerId(),
                            workerID,
                            rating.getConstraintsList());
                }
        ).map(status -> {
            logger.debug("Object service answered with status {}.", status);
            return String.valueOf(status);
        });
    }

    /**
     * if the throwable is not null it wraps the throwable into an InternalServerErrorException, or else it sets the
     * response to 201 Created.
     *
     * @param t         the Data to pass
     * @param throwable the throwable to wrap
     * @param context the Context of the Request
     * @param <T>       the type of the data
     * @return maybe the data (if the throwable is not null
     */
    private <T> T wrapExceptionOr201(T t, Throwable throwable, Context context) {
        if (throwable != null) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new InternalServerErrorException("an error occurred while communicating with the Object-Service", throwable);
        } else {
            context.getResponse().status(201);
            return t;
        }
    }

    /**
     * merges the requests body with the Builder
     *
     * @param context the Context of the Request with the JSON Representation of the Message as a Body
     * @param t       the builder
     * @param excluded the field numbers of the optional fields
     * @param <T>     the type of the builder
     * @return the builder with everything set
     * @throws BadRequestException if an error occurred while parsing the JSON or wrong content-type
     */
    private <T extends Message.Builder> Promise<T> merge(Context context, T t, List<Integer> excluded) throws BadRequestException {
        assertJson(context);
        return context.getRequest().getBody()
                .map(TypedData::getText)
                .map(body -> {
                    try {
                        parser.merge(body, t);
                    } catch (InvalidProtocolBufferException e) {
                        throw new BadRequestException("error while parsing JSON", e);
                    }
                    t.getDescriptorForType().getFields().stream()
                            .filter(field -> !field.isRepeated() && !t.hasField(field))
                            .filter(field -> !excluded.contains(field.getNumber()))
                            .findAny()
                            .ifPresent(field -> {
                                throw new BadRequestException("field must be set:" + field.getFullName());
                            });
                    return t;
                });
    }

    /**
     * the abstraction for the usual submit.
     * merges the body of the request with the builder, checks the fields and then calls the function. After calling
     * the function it wraps
     * the eventual exception, sets the status accordingly and blocks for return.
     *
     * @param context the Context of the Request
     * @param x        the builder for the expected message
     * @param func     the function to execute
     * @param <X>      the type of the builder
     * @param <T>      the type of the return data
     * @return an future instance of T or an exception
     */
    private <X extends Message.Builder, T> Promise<T> doSubmit(Context context, X x,
                                                      BiFunction<X, Integer, CompletableFuture<T>> func) {
        return doSubmit(context, x, new ArrayList<>(), func);
    }

    /**
     * the abstraction for the usual submit.
     * merges the body of the request with the builder, checks the fields and then calls the function. After calling
     * the function it wraps
     * the eventual exception, sets the status accordingly and blocks for return.
     *
     * @param context the Context of the Request
     * @param x        the builder for the expected message
     * @param excluded the field numbers of the optional fields
     * @param func     the function to execute
     * @param <X>      the type of the builder
     * @param <T>      the type of the return data
     * @return an future instance of T or an exception
     */
    private <X extends Message.Builder, T> Promise<T> doSubmit(Context context, X x,
                                                      List<Integer> excluded,
                                                      BiFunction<X, Integer, CompletableFuture<T>> func) {
        int workerID = context.get(WorkerID.class).get();
        return merge(context, x, excluded)
                .flatMap(builder ->
                        Promise.of(downstream -> downstream.accept(
                                func.apply(builder, workerID)
                                        .handle((t, throwable) -> wrapExceptionOr201(t, throwable, context))))
                );
    }

    /**
     * Http HEAD Method to get URL content type
     *
     * @param urlString the url to check the content type
     * @return the content type
     * @throws IOException if an error occured while trying to probe the content type
     */
    //from: http://stackoverflow.com/questions/5801993/quickest-way-to-get-content-type
    public static String getContentType(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        if (isRedirect(connection.getResponseCode())) {
            String newUrl = connection.getHeaderField("Location"); // get redirect url from "location" header field
            return getContentType(newUrl);
        }
        return connection.getContentType();
    }

    /**
     * Check status code for redirects
     *
     * @param statusCode the status code for the redirects
     * @return true if matched redirect group
     */
    //from: http://stackoverflow.com/questions/5801993/quickest-way-to-get-content-type
    protected static boolean isRedirect(int statusCode) {
        if (statusCode != HttpURLConnection.HTTP_OK) {
            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                    || statusCode == HttpURLConnection.HTTP_SEE_OTHER) {
                return true;
            }
        }
        return false;
    }
}
