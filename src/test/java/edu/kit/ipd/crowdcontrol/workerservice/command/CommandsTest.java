package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Answer;
import edu.kit.ipd.crowdcontrol.workerservice.proto.EmailAnswer;
import org.jooq.lambda.function.Function3;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import spark.Request;
import spark.Response;

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

    private Commands prepareCommands(String answerType, int experimentID, Communication communication) {
        ExperimentRecord record = mock(ExperimentRecord.class);
        when(record.getAnswerType()).thenReturn(answerType);
        ExperimentOperations operation = mock(ExperimentOperations.class);
        when(operation.getExperiment(experimentID)).thenReturn(record);
        return new Commands(communication, operation);
    }

    private Commands prepareCommands(int experimentID, Communication communication) {
        return prepareCommands(null, experimentID, communication);
    }

    private Commands prepareCommands(Communication communication) {
        return prepareCommands(1, communication);
    }

    private <T> T testSubmit(int task, String answerType, Consumer<Communication> buildCommunication,
                             Consumer<Request> buildRequest, Function3<Commands, Request, Response, T> func,
                             Consumer<Response> responseVerifier) {
        Communication communication = mock(Communication.class);
        buildCommunication.accept(communication);
        Commands commands = prepareCommands(answerType, task,communication);
        Request request = mock(Request.class);
        buildRequest.accept(request);
        Response response = mock(Response.class);
        T apply = func.apply(commands, request, response);
        if (responseVerifier != null)
            responseVerifier.accept(response);
        return apply;
    }

    private <T> T testSubmit(Consumer<Communication> buildCommunication,
                             Consumer<Request> buildRequest, Function3<Commands, Request, Response, T> func,
                             Consumer<Response> responseVerifier) {
        return testSubmit(1, null, buildCommunication, buildRequest, func, responseVerifier);
    }

    private String testSubmitEmailHelper(String email, String platform, int workerID, Consumer<Response> responseVerifier) {
        return testSubmit(communication -> {
                    when(communication.submitWorker(email, platform)).thenReturn(CompletableFuture.completedFuture(workerID));
                },
                request -> {
                    when(request.params("platform")).thenReturn(platform);
                    when(request.body()).thenReturn(email);
                },
                Commands::submitEmail,
                responseVerifier
        );
    }

    private Object testSubmitAnswerHelper(String answer, String answerRequest, int task, int workerID, int answerID, Consumer<Response> responseVerifier) {
        return testSubmit(task, null,
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

    private Object testSubmitAnswerHelper(String answer, int task, int workerID, int answerID, Consumer<Response> responseVerifier) throws Exception {
        return testSubmitAnswerHelper(answer, printer.print(Answer.newBuilder()
                .setTask(task)
                .setAnswer(answer)
                .build()),
                task, workerID, answerID, responseVerifier);
    }

    private <T> T nonJson(Function3<Commands, Request, Response, T> func) {
        Communication communication = mock(Communication.class);
        Commands commands = prepareCommands(communication);
        Request request = mock(Request.class);
        when(request.params("platform")).thenReturn("a");
        when(request.contentType()).thenReturn("x");
        Response response = mock(Response.class);
        exception.expect(BadRequestException.class);
        return func.apply(commands, request, response);
    }


    @Test
    public void testFullSubmitEmail() throws Exception {
        String email =  "x.y@z.de";
        String platform = "a";
        int workerID = 1;
        String json = testSubmitEmailHelper(email, platform, workerID, response -> verify(response).status(201));
        EmailAnswer.Builder builder = EmailAnswer.newBuilder();
        parser.merge(json, builder);
        assertEquals(builder.getWorkerId(), workerID);
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMalformedEmail() throws Exception {
        String email =  "x.y@z.de@";
        String platform = "a";
        int workerID = 1;
        testSubmitEmailHelper(email, platform, workerID, response -> verify(response).status(201));
    }

    @Test
    public void testSubmitCalibration() throws Exception {

    }

    @Test
    public void testSubmitCalibrationNonJson() throws Exception {
        nonJson(Commands::submitCalibration);
    }

    @Test
    public void testSubmitFullAnswer() throws Exception {
        String answer =  "example-answer";
        int workerID = 1;
        int task = 2;
        int answerID = 3;
        Consumer<Response> responseVerifier = response -> verify(response).status(201);
        testSubmitAnswerHelper(answer, task, workerID, answerID, responseVerifier);
        String answerRequest =  "{\n" +
                "\"answer\": \""+ answer + "\",\n" +
                "\"task\": " + task + "\n" +
                "}";
        testSubmitAnswerHelper(answer, answerRequest, task, workerID, answerID, responseVerifier);
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
        testSubmitAnswerHelper(answer, answerRequest, task, workerID, answerID, response -> verify(response).status(201));
    }

    @Test(expected= BadRequestException.class)
    public void testSubmitMissingAnswer2() throws Exception {
        JsonFormat.printer();
        String answer =  "example-answer";
        int workerID = 1;
        int task = 2;
        int answerID = 3;
        String answerRequest =  "{\n" +
                "\"task\": " + task + "\n" +
                "}";
        testSubmitAnswerHelper(answer, answerRequest, task, workerID, answerID, response -> verify(response).status(201));
    }

    @Test
    public void testSubmitAnswerNonJson() throws Exception {
        nonJson(Commands::submitAnswer);
    }

    @Test
    public void testSubmitRating() throws Exception {

    }

    @Test
    public void testSubmitRatingNonJson() throws Exception {
        nonJson(Commands::submitRating);
    }
}