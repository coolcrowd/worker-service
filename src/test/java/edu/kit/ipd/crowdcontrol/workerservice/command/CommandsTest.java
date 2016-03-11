package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.common.collect.LinkedListMultimap;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.JWTHelper;
import edu.kit.ipd.crowdcontrol.workerservice.WorkerID;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.http.*;
import ratpack.path.PathTokens;
import ratpack.test.exec.ExecHarness;
import ratpack.util.MultiValueMap;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
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

    ExecHarness execHarness = ExecHarness.harness();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test(expected= Exception.class)
    public void testCommunicationError() throws Exception {
        String email =  "x.y@z.de";
        String platform = "a";
        submit(communication -> {
                    when(communication.submitWorker(email, platform, LinkedListMultimap.create())).thenReturn(CompletableFuture.supplyAsync(() -> {
                        throw new RuntimeException("example Exception");
                    }));
                },
                context -> {
                    when(context.getPathTokens().get("platform")).thenReturn(platform);
                    TypedData data = mock(TypedData.class);
                    when(data.getText()).thenReturn(email);
                    when(context.getRequest().getBody()).thenReturn(Promise.value(data));
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
        EmailAnswer builder = submitEmailHelper(email, platform, workerID, context -> verify(context.getResponse()).status(201));
        assertEquals(Integer.parseInt(builder.getAuthorization()), workerID);
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMalformedEmail() throws Exception {
        String email =  "x.y@z.de@";
        String platform = "a";
        int workerID = 1;
        submitEmailHelper(email, platform, workerID, context -> verify(context.getResponse()).status(201));
    }

    @Test
    public void testSubmitFullCalibration() throws Exception {
        int workerID = 1;
        int task = 2;
        int option = 2;
        submitCalibrationHelper(option, Calibration.newBuilder()
                .setAnswerOption(option)
                .build(),
                task, workerID, context -> verify(context.getResponse()).status(201)
        );
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMissingCalibration() throws Exception {
        int workerID = 1;
        int task = 2;
        int option = 2;
        submitCalibrationHelper(option, Calibration.newBuilder()
                        .build(),
                task, workerID, context -> verify(context.getResponse()).status(201)
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
                experiment, workerID, answerID, context -> verify(context.getResponse()).status(201));
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
        submitAnswerHelper(answer, answerRequest, task, workerID, answerID, context -> verify(context.getResponse()).status(201));
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
        submitAnswerHelper(answer, answerRequest, task, workerID, answerID, context -> verify(context.getResponse()).status(201));
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
        submitAnswerHelper(answer, answerRequest, task, workerID, answerID, context -> verify(context.getResponse()).status(201));
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
                        .setRatingId(ratingID)
                        .build(),
                experiment, answerID, workerID, ratingID, context -> verify(context.getResponse()).status(201));
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
                experiment, answerID, workerID, ratingID, context -> verify(context.getResponse()).status(201));
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
                experiment, answerID, workerID, ratingID, context -> verify(context.getResponse()).status(201));
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
        JWTHelper jwtMock = mock(JWTHelper.class);
        when(jwtMock.generateJWT(anyInt())).thenAnswer(invocation -> String.valueOf(invocation.getArguments()[0]));
        return new Commands(communication, operation, jwtMock);
    }

    Commands prepareCommands(int experimentID, Communication communication) {
        return prepareCommands(null, experimentID, communication);
    }

    Commands prepareCommands(Communication communication) {
        return prepareCommands(1, communication);
    }

    <T> T submit(int task, String answerType, Consumer<Communication> buildCommunication,
                         Consumer<Context> buildRequest, BiFunction<Commands, Context, Promise<T>> func,
                         Consumer<Context> responseVerifier) throws Exception {
        Communication communication = mock(Communication.class);
        buildCommunication.accept(communication);
        Commands commands = prepareCommands(answerType, task,communication);
        Context context = mock(Context.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Headers headers = mock(Headers.class);
        when(context.getRequest()).thenReturn(request);
        when(context.getResponse()).thenReturn(response);
        when(request.getHeaders()).thenReturn(headers);
        when(headers.get("accept")).thenReturn("application/json");
        MediaType mediaType = mock(MediaType.class);
        when(request.getContentType()).thenReturn(mediaType);
        when(mediaType.getType()).thenReturn("application/json");
        MultiValueMap<String, String> mock = mock(MultiValueMap.class);
        when(mock.asMultimap()).thenReturn(LinkedListMultimap.create());
        when(request.getQueryParams()).thenReturn(mock);
        PathTokens pathTokens = mock(PathTokens.class);
        when(context.getPathTokens()).thenReturn(pathTokens);
        buildRequest.accept(context);
        T result = execHarness.yield(e -> func.apply(commands, context)).getValueOrThrow();
        if (responseVerifier != null)
            responseVerifier.accept(context);
        return result;
    }

    <T> T submit(Consumer<Communication> buildCommunication,
                         Consumer<Context> buildRequest, BiFunction<Commands, Context, Promise<T>> func,
                         Consumer<Context> responseVerifier) throws Exception {
        return submit(1, null, buildCommunication, buildRequest, func, responseVerifier);
    }

    EmailAnswer submitEmailHelper(String email, String platform, int workerID, Consumer<Context> responseVerifier) throws Exception {
        return submit(communication -> {
                    when(communication.submitWorker(email, platform, LinkedListMultimap.create())).thenReturn(CompletableFuture.completedFuture(workerID));
                },
                context -> {
                    when(context.getPathTokens().get("platform")).thenReturn(platform);
                    Email build = Email.newBuilder().setEmail(email).build();
                    TypedData data = mock(TypedData.class);
                    try {
                        when(data.getText()).thenReturn(printer.print(build));
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                    when(context.getRequest().getBody()).thenReturn(Promise.value(data));
                },
                Commands::submitEmail,
                responseVerifier
        );
    }

    Object submitAnswerHelper(String answer, String answerRequest, int task, int workerID, int answerID, Consumer<Context> responseVerifier) throws Exception {
        return submit(task, null,
                communication -> {
                    when(communication.submitAnswer(answer, 0, task, workerID))
                            .thenReturn(CompletableFuture.completedFuture(answerID));
                },
                context -> {
                    WorkerID workerIDClass = new WorkerID(workerID);
                    when(context.get(WorkerID.class)).thenReturn(workerIDClass);
                    TypedData data = mock(TypedData.class);
                    when(data.getText()).thenReturn(answerRequest);
                    when(context.getRequest().getBody()).thenReturn(Promise.value(data));
                },
                Commands::submitAnswer,
                responseVerifier
        );
    }

    Object submitRatingHelper(int rating, Rating ratingRequest, int task, int answer, int workerID, int ratingID, Consumer<Context> responseVerifier) throws Exception {
        String json = printer.print(ratingRequest);
        return submit(task, null,
                communication -> {
                    when(communication.submitRating(ratingID, rating, "", task, answer, workerID, new ArrayList<>()))
                            .thenReturn(CompletableFuture.completedFuture(201));
                },
                context -> {
                    WorkerID workerIDClass = new WorkerID(workerID);
                    when(context.get(WorkerID.class)).thenReturn(workerIDClass);
                    TypedData data = mock(TypedData.class);
                    when(data.getText()).thenReturn(json);
                    when(context.getRequest().getBody()).thenReturn(Promise.value(data));
                },
                Commands::submitRating,
                responseVerifier
        );
    }

    Object submitCalibrationHelper(int option, Calibration calibrationRequest, int task, int workerID, Consumer<Context> responseVerifier) throws Exception {
        String json = printer.print(calibrationRequest);
        return submit(task, null,
                communication -> {
                    when(communication.submitCalibration(option, workerID))
                            .thenReturn(CompletableFuture.completedFuture(null));
                },
                context -> {
                    WorkerID workerIDClass = new WorkerID(workerID);
                    when(context.get(WorkerID.class)).thenReturn(workerIDClass);
                    TypedData data = mock(TypedData.class);
                    when(data.getText()).thenReturn(json);
                    when(context.getRequest().getBody()).thenReturn(Promise.value(data));
                },
                Commands::submitCalibration,
                responseVerifier
        );
    }

    <T> T nonJson(BiFunction<Commands, Context, Promise<T>> func) throws Exception {
        Communication communication = mock(Communication.class);
        Context context = mock(Context.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Headers headers = mock(Headers.class);
        when(context.getRequest()).thenReturn(request);
        when(context.getResponse()).thenReturn(response);
        when(request.getHeaders()).thenReturn(headers);
        when(headers.get("accept")).thenReturn("x");
        MediaType mediaType = mock(MediaType.class);
        when(request.getContentType()).thenReturn(mediaType);
        when(mediaType.getType()).thenReturn("x");
        MultiValueMap<String, String> mock = mock(MultiValueMap.class);
        when(mock.asMultimap()).thenReturn(LinkedListMultimap.create());
        when(request.getQueryParams()).thenReturn(mock);
        PathTokens pathTokens = mock(PathTokens.class);
        when(context.getPathTokens()).thenReturn(pathTokens);
        when(context.getPathTokens().get("platform")).thenReturn(String.valueOf("a"));
        WorkerID workerIDClass = new WorkerID(3);
        when(context.get(WorkerID.class)).thenReturn(workerIDClass);
        Commands commands = prepareCommands(communication);
        exception.expect(BadRequestException.class);
        return Blocking.on(func.apply(commands, context));
    }
}