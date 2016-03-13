package edu.kit.ipd.crowdcontrol.workerservice.command;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.proto.Answer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.http.TypedData;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

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
                experiment, workerID, answerID, context -> verify(context, times(2)).getResponse().status(201));
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
                experiment, workerID, answerID, context -> verify(context, times(2)).getResponse().status(201));
    }

    private Object submitAnswerHelper(String answer, String description, String answerRequest, int task, int workerID, int answerID, Consumer<Context> responseVerifier) throws Exception {
        return commandsTest.submit(task, description,
                communication -> {
                    when(communication.submitAnswer(answer, 0, task, workerID))
                            .thenReturn(CompletableFuture.completedFuture(answerID));
                },
                context -> {
                    when(context.getPathTokens().get("workerID")).thenReturn(String.valueOf(workerID));
                    TypedData data = mock(TypedData.class);
                    when(data.getText()).thenReturn(answerRequest);
                    when(context.getRequest().getBody()).thenReturn(Promise.value(data));
                },
                Commands::submitAnswer,
                responseVerifier
        );
    }
}