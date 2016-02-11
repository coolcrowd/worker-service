package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Answer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import spark.Response;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class CommandsIntegrationTest {
    private final JsonFormat.Printer printer = JsonFormat.printer();
    private final CommandsTest commandsTest = new CommandsTest();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testSubmitFullAnswer() throws Exception {
        String answer =  "http://placehold.it/350x150";
        int workerID = 1;
        int experiment = 2;
        int answerID = 3;
        submitAnswerHelper(answer, "image/*", printer.print(Answer.newBuilder()
                .setExperiment(experiment)
                .setAnswer(answer)
                .build()),
                experiment, workerID, answerID, response -> verify(response, times(2)).status(201));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testSubmitFalseAnswer() throws Exception {
        String answer =  "www.google.de";
        int workerID = 1;
        int experiment = 2;
        int answerID = 3;
        submitAnswerHelper(answer, "image", printer.print(Answer.newBuilder()
                        .setExperiment(experiment)
                        .setAnswer(answer)
                        .build()),
                experiment, workerID, answerID, response -> verify(response, times(2)).status(201));
    }

    private Object submitAnswerHelper(String answer, String description, String answerRequest, int task, int workerID, int answerID, Consumer<Response> responseVerifier) {
        return commandsTest.submit(task, description,
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
}