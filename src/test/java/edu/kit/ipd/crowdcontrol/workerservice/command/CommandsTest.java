package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Answer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Calibration;
import edu.kit.ipd.crowdcontrol.workerservice.proto.EmailAnswer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Rating;
import org.jooq.lambda.function.Function3;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author LeanderK
 * @version 1.0
 */
public class CommandsTest {
    private final JsonFormat.Printer printer = JsonFormat.printer();
    private final JsonFormat.Parser parser = JsonFormat.parser();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test(expected= Exception.class)
    public void testCommunicationError() throws Exception {
        String email =  "x.y@z.de";
        String platform = "a";
        submit(communication -> {
                    when(communication.submitWorker(email, platform, new HashMap<>())).thenReturn(CompletableFuture.supplyAsync(() -> {
                        throw new RuntimeException("example Exception");
                    }));
                },
                request -> {
                    when(request.params("platform")).thenReturn(platform);
                    when(request.body()).thenReturn(email);
                },
                Commands::submitEmail,
                null
        );
    }

    @Test
    public void testFullSubmitEmail() throws Exception {
        String email =  "x.y@z.de";
        String platform = "a";
        int workerID = 1;
        String json = submitEmailHelper(email, platform, workerID, response -> verify(response).status(201));
        EmailAnswer.Builder builder = EmailAnswer.newBuilder();
        parser.merge(json, builder);
        assertEquals(builder.getWorkerId(), workerID);
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMalformedEmail() throws Exception {
        String email =  "x.y@z.de@";
        String platform = "a";
        int workerID = 1;
        submitEmailHelper(email, platform, workerID, response -> verify(response).status(201));
    }

    @Test
    public void testSubmitFullCalibration() throws Exception {
        int workerID = 1;
        int task = 2;
        int option = 2;
        submitCalibrationHelper(option, Calibration.newBuilder()
                .setAnswerOption(option)
                .build(),
                task, workerID, response -> verify(response).status(201)
        );
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMissingCalibration() throws Exception {
        int workerID = 1;
        int task = 2;
        int option = 2;
        submitCalibrationHelper(option, Calibration.newBuilder()
                        .build(),
                task, workerID, response -> verify(response).status(201)
        );
    }

    @Test
    public void testSubmitCalibrationNonJson() throws Exception {
        nonJson(Commands::submitCalibration);
    }

    @Test
    public void testSubmitFullAnswer() throws Exception {
        String answer =  "example-answer";
        int workerID = 1;
        int experiment = 2;
        int answerID = 3;
        submitAnswerHelper(answer, printer.print(Answer.newBuilder()
                .setExperiment(experiment)
                .setAnswer(answer)
                .build()),
                experiment, workerID, answerID, response -> verify(response).status(201));
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMissingAnswer() throws Exception {
        String answer =  "example-answer";
        int workerID = 1;
        int task = 2;
        int answerID = 3;
        String answerRequest =  "{\n" +
                " \"answer\": \""+ answer + "\"\n" +
                "}";
        submitAnswerHelper(answer, answerRequest, task, workerID, answerID, response -> verify(response).status(201));
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMissingAnswer2() throws Exception {
        String answer =  "example-answer";
        int workerID = 1;
        int task = 2;
        int answerID = 3;
        String answerRequest =  "{\n" +
                "\"task\": " + task + "\n" +
                "}";
        submitAnswerHelper(answer, answerRequest, task, workerID, answerID, response -> verify(response).status(201));
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitEmptyAnswer() throws Exception {
        JsonFormat.printer();
        String answer =  "example-answer";
        int workerID = 1;
        int task = 2;
        int answerID = 3;
        String answerRequest =  "{\n" +
                "}";
        submitAnswerHelper(answer, answerRequest, task, workerID, answerID, response -> verify(response).status(201));
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitWithStringWorkerID() throws Exception {
        JsonFormat.printer();
        String workerID = "aaa";
        int task = 2;
        String answerRequest =  "{\n" +
                "}";
        submit(task, null,
                ign -> {},
                request -> {
                    when(request.params("workerID")).thenReturn(workerID);
                    when(request.body()).thenReturn(answerRequest);
                    when(request.contentType()).thenReturn("application/json");
                },
                Commands::submitAnswer,
                response -> {
                    verify(response).status(201);
                }
        );
    }

    @Test
    public void testSubmitAnswerNonJson() throws Exception {
        nonJson(Commands::submitAnswer);
    }

    @Test
    public void testSubmitFullRating() throws Exception {
        int workerID = 1;
        int experiment = 2;
        int answerID = 3;
        int rating = 12;
        int ratingID = 15;
        submitRatingHelper(rating, Rating.newBuilder()
                        .setRating(rating)
                        .setAnswerId(answerID)
                        .setExperiment(experiment)
                        .build(),
                experiment, answerID, workerID, ratingID, response -> verify(response).status(201));
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMissingRating1() throws Exception {
        int workerID = 1;
        int experiment = 2;
        int answerID = 3;
        int rating = 12;
        int ratingID = 15;
        submitRatingHelper(rating, Rating.newBuilder()
                        .setAnswerId(answerID)
                        .setExperiment(experiment)
                        .build(),
                experiment, answerID, workerID, ratingID, response -> verify(response).status(201));
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMissingRating2() throws Exception {
        int workerID = 1;
        int experiment = 2;
        int answerID = 3;
        int rating = 12;
        int ratingID = 15;
        submitRatingHelper(rating, Rating.newBuilder()
                        .setRating(rating)
                        .setExperiment(experiment)
                        .build(),
                experiment, answerID, workerID, ratingID, response -> verify(response).status(201));
    }

    @Test
    public void testSubmitRatingNonJson() throws Exception {
        nonJson(Commands::submitRating);
    }

    Commands prepareCommands(String answerType, int experimentID, Communication communication) {
        ExperimentRecord record = mock(ExperimentRecord.class);
        when(record.getAnswerType()).thenReturn(answerType);
        ExperimentOperations operation = mock(ExperimentOperations.class);
        when(operation.getExperiment(experimentID)).thenReturn(record);
        return new Commands(communication, operation);
    }

    Commands prepareCommands(int experimentID, Communication communication) {
        return prepareCommands(null, experimentID, communication);
    }

    Commands prepareCommands(Communication communication) {
        return prepareCommands(1, communication);
    }

    <T> T submit(int task, String answerType, Consumer<Communication> buildCommunication,
                         Consumer<Request> buildRequest, Function3<Commands, Request, Response, T> func,
                         Consumer<Response> responseVerifier) {
        Communication communication = mock(Communication.class);
        buildCommunication.accept(communication);
        Commands commands = prepareCommands(answerType, task,communication);
        Request request = mock(Request.class);
        when(request.headers("accept")).thenReturn("application/json");
        QueryParamsMap query = mock(QueryParamsMap.class);
        when(request.queryMap()).thenReturn(query);
        buildRequest.accept(request);
        Response response = mock(Response.class);
        T apply = func.apply(commands, request, response);
        if (responseVerifier != null)
            responseVerifier.accept(response);
        return apply;
    }

    <T> T submit(Consumer<Communication> buildCommunication,
                         Consumer<Request> buildRequest, Function3<Commands, Request, Response, T> func,
                         Consumer<Response> responseVerifier) {
        return submit(1, null, buildCommunication, buildRequest, func, responseVerifier);
    }

    String submitEmailHelper(String email, String platform, int workerID, Consumer<Response> responseVerifier) {
        return submit(communication -> {
                    when(communication.submitWorker(email, platform, new HashMap<>())).thenReturn(CompletableFuture.completedFuture(workerID));
                },
                request -> {
                    when(request.params("platform")).thenReturn(platform);
                    when(request.body()).thenReturn(email);
                },
                Commands::submitEmail,
                responseVerifier
        );
    }

    Object submitAnswerHelper(String answer, String answerRequest, int task, int workerID, int answerID, Consumer<Response> responseVerifier) {
        return submit(task, null,
                communication -> {
                    when(communication.submitAnswer(answer, task, workerID))
                            .thenReturn(CompletableFuture.completedFuture(answerID));
                },
                request -> {
                    when(request.params("workerID")).thenReturn(String.valueOf(workerID));
                    when(request.body()).thenReturn(answerRequest);
                    when(request.contentType()).thenReturn("application/json");
                },
                Commands::submitAnswer,
                responseVerifier
        );
    }

    Object submitRatingHelper(int rating, Rating ratingRequest, int task, int answer, int workerID, int ratingID, Consumer<Response> responseVerifier) throws InvalidProtocolBufferException {
        String json = printer.print(ratingRequest);
        return submit(task, null,
                communication -> {
                    when(communication.submitRating(rating, "", task, answer, workerID))
                            .thenReturn(CompletableFuture.completedFuture(null));
                },
                request -> {
                    when(request.params("workerID")).thenReturn(String.valueOf(workerID));
                    when(request.body()).thenReturn(json);
                    when(request.contentType()).thenReturn("application/json");
                },
                Commands::submitRating,
                responseVerifier
        );
    }

    Object submitCalibrationHelper(int option, Calibration calibrationRequest, int task, int workerID, Consumer<Response> responseVerifier) throws InvalidProtocolBufferException {
        String json = printer.print(calibrationRequest);
        return submit(task, null,
                communication -> {
                    when(communication.submitCalibration(option, workerID))
                            .thenReturn(CompletableFuture.completedFuture(null));
                },
                request -> {
                    when(request.params("workerID")).thenReturn(String.valueOf(workerID));
                    when(request.body()).thenReturn(json);
                    when(request.contentType()).thenReturn("application/json");
                },
                Commands::submitCalibration,
                responseVerifier
        );
    }

    <T> T nonJson(Function3<Commands, Request, Response, T> func) {
        Communication communication = mock(Communication.class);
        Commands commands = prepareCommands(communication);
        Request request = mock(Request.class);
        when(request.params("platform")).thenReturn("a");
        when(request.params("workerID")).thenReturn("3");
        when(request.contentType()).thenReturn("x");
        Response response = mock(Response.class);
        exception.expect(BadRequestException.class);
        return func.apply(commands, request, response);
    }
}