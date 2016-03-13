package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.JWTHelper;
import edu.kit.ipd.crowdcontrol.workerservice.WorkerID;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformModeMode;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.CalibrationAnswerOptionRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.CalibrationRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PlatformRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.*;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.jooq.Result;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ratpack.handling.Context;
import ratpack.http.Headers;
import ratpack.http.MediaType;
import ratpack.http.Request;
import ratpack.http.Response;
import ratpack.path.PathTokens;
import ratpack.util.MultiValueMap;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class QueriesTest {

    private OperationsDataHolder data;

    @Before
    public void setUp() {
        data = new OperationsDataHolder();
    }

    @Test
    public void testExpectEmail() throws Exception {
        Context context = prepareContext(data.getExperimentRecord().getIdExperiment(), data.getPlatformRecord().getIdPlatform(), -1);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        View view = queries.getNext(context);
        assertTrue(view.getType().equals(View.Type.EMAIL));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testExpectEmailFail() throws Exception {
        data.getPlatformRecord().setNeedsEmail(false);
        Context context = prepareContext(data.getExperimentRecord().getIdExperiment(), data.getPlatformRecord().getIdPlatform(), -1);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        queries.getNext(context);
    }

    @Test
    public void testExpectGetWorkerID() throws Exception {
        data.setBelongsToWrongPopulation(true);
        data.getPlatformRecord().setNeedsEmail(false);
        Context context = prepareContext(data.getExperimentRecord().getIdExperiment(), data.getPlatformRecord().getIdPlatform(), -1);
        final Optional<WorkerID>[] inContext = new Optional[1];
        inContext[0] = Optional.empty();
        when(context.getRequest().add(WorkerID.class, new WorkerID(data.getWorkerID())))
                .then(invocation -> {
                    inContext[0] = Optional.of((WorkerID) invocation.getArguments()[1]);
                    return null;
                });
        when(context.get(WorkerID.class)).thenAnswer(invocation -> inContext[0].get());
        when(context.maybeGet(WorkerID.class)).thenAnswer(invocation -> inContext[0]);

        Queries queries =  prepareQuery(data, Optional.of(data.getWorkerID()), null);
        View view = queries.getNext(context);
        assertTrue(Integer.parseInt(view.getAuthorization()) == data.getWorkerID());
    }


    @Test
    public void testProvideWorkerID() throws Exception {
        Context context = prepareContext(data);
        Queries queries = prepareQuery(data, Optional.empty(), null);
        View view = queries.getNext(context);
        assertTrue(view.getType().equals(View.Type.CALIBRATION));
    }


    @Test
    public void testCalibrations() throws Exception {
        Context context = prepareContext(data);
        Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> calibrations = data.getCalibrations();
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        View view = queries.getNext(context);
        assertTrue(view.getType().equals(View.Type.CALIBRATION));
        assertTrue(view.getCalibrationsList().size() == calibrations.size());
        Optional<Map.Entry<CalibrationRecord, Result<CalibrationAnswerOptionRecord>>> res = calibrations.entrySet().stream()
                .filter(entry -> !view.getCalibrationsList().stream()
                        .filter(calibration -> calibration.getQuestion().equals(entry.getKey().getProperty()))
                        .filter(calibration -> calibration.getId() == entry.getKey().getIdCalibration())
                        .flatMap(calibration -> calibration.getAnswerOptionsList().stream())
                        .filter(answerOption ->
                                entry.getValue().stream().anyMatch(record ->
                                        record.getAnswer().equals(answerOption.getOption())))
                        .anyMatch(answerOption ->
                                entry.getValue().stream().anyMatch(record ->
                                        record.getIdCalibrationAnswerOption().equals(answerOption.getId())))
                ).findAny();
        assertTrue(!res.isPresent());
    }

    @Test
    public void testWrongCalibration() throws Exception {
        data.setBelongsToWrongPopulation(true);
        Context context = prepareContext(data);
        Queries queries = prepareQuery(data, Optional.empty(), null);
        View view = queries.getNext(context);
        assertTrue(view.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testInvalidQuality() throws Exception {
        data.setWorkerQuality(data.getExperimentRecord().getWorkerQualityThreshold() - 1);
        Context context = prepareContext(data);
        Queries queries = prepareQuery(data, Optional.empty(), null);
        View view = queries.getNext(context);
        assertTrue(view.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testSkipAll() throws Exception {
        data.setCalibrations(new HashMap<>());
        Context context = prepareContext(data);
        when(context.getRequest().getQueryParams().get("answer")).thenReturn("skip");
        when(context.getRequest().getQueryParams().get("rating")).thenReturn("skip");
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        View view = queries.getNext(context);
        assertTrue(view.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testCreative() throws Exception {
        View.Builder builder = testWithTaskChooser(false, true);
        assertTrue(builder.getType().equals(View.Type.ANSWER));
    }

    @Test
    public void testRating() throws Exception {
        View.Builder builder = testWithTaskChooser(false, false);
        assertTrue(builder.getType().equals(View.Type.RATING));
    }

    @Test
    public void testStopgapAnswer() throws Exception {
        data.getExperimentsPlatformModeRecord().setMode(ExperimentsPlatformModeMode.answer);
        View.Builder builder = testWithTaskChooser(false, false);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testStopgapAnswerRating() throws Exception {
        data.getExperimentsPlatformModeRecord().setMode(ExperimentsPlatformModeMode.rating);
        View.Builder builder = testWithTaskChooser(false, true);
        assertTrue(builder.getType().equals(View.Type.RATING));
    }

    @Test
    public void testPreview() throws Exception {
        Context context = prepareContext(data);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        View view = queries.preview(context);
        assertTrue(view.getType().equals(View.Type.ANSWER));
        assertTrue(view.getTitle().equals(data.getExperimentRecord().getTitle()));
        assertTrue(view.getDescription().equals(data.getExperimentRecord().getDescription()));
        assertTrue(view.getConstraintsList().size() == data.getConstraints().size());
    }

    private View.Builder testWithTaskChooser(boolean finish, boolean creative) throws Exception {
        data.setCalibrations(new HashMap<>());
        Context context = prepareContext(data);
        String mockTaskChooserDescription = "mockTaskChooserDescription";
        JWTHelper jwtMock = mock(JWTHelper.class);
        when(jwtMock.generateJWT(anyInt())).thenAnswer(invocation -> String.valueOf(invocation.getArguments()[0]));
        data.getExperimentRecord().setAlgorithmTaskChooser(mockTaskChooserDescription);
        ExperimentOperations experimentOperations = data.createExperimentOperations();
        ExperimentsPlatformOperations experimentsPlatformOperations = data.createExperimentsPlatformOperations();
        CalibrationsOperations calibrationsOperations = data.createPopulationsOperations();
        PlatformOperations platformOperations = data.createPlatformOperations();
        Communication communication = prepareCommunication(data.getPlatformRecord(), Optional.empty());
        MockTaskChooser mockTaskChooser = new MockTaskChooser(finish, creative, data.getExperimentRecord(), experimentOperations, experimentsPlatformOperations);
        Queries queries =  new Queries(calibrationsOperations, experimentOperations, platformOperations, communication, experimentsPlatformOperations, mockTaskChooser, data.createWorkerOperations(), jwtMock);
        View view = queries.getNext(context);
        return view.toBuilder();
    }

    private Queries prepareQuery(OperationsDataHolder data, Optional<Integer> communicationReturn, TaskChooserAlgorithm taskChooserAlgorithm) {
        Communication communication = prepareCommunication(data.getPlatformRecord(), communicationReturn);
        JWTHelper jwtMock = mock(JWTHelper.class);
        when(jwtMock.generateJWT(anyInt())).thenAnswer(invocation -> String.valueOf(invocation.getArguments()[0]));
        return new Queries(data.createPopulationsOperations(), data.createExperimentOperations(), data.createPlatformOperations(),
                communication, data.createExperimentsPlatformOperations(), taskChooserAlgorithm, data.createWorkerOperations(), jwtMock);
    }

    private Context prepareContext(OperationsDataHolder dataHolder) {
        return prepareContext(dataHolder.getExperimentRecord().getIdExperiment(), dataHolder.getPlatformRecord().getIdPlatform(), dataHolder.getWorkerID());
    }

    private Context prepareContext(int experimentID, String platform, int workerID) {
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
        when(mediaType.getType()).thenReturn("x");
        when(request.getQueryParams()).thenReturn(mock(MultiValueMap.class));
        PathTokens pathTokens = mock(PathTokens.class);
        when(context.getPathTokens()).thenReturn(pathTokens);
        when(context.getPathTokens().get("platform")).thenReturn(String.valueOf(platform));
        when(context.getPathTokens().get("experiment")).thenReturn(String.valueOf(experimentID));
        if (workerID != -1) {
            WorkerID workerIDClass = new WorkerID(workerID);
            when(context.get(WorkerID.class)).thenReturn(workerIDClass);
            when(context.maybeGet(WorkerID.class)).thenReturn(Optional.of(workerIDClass));
        } else {
            when(context.maybeGet(WorkerID.class)).thenReturn(Optional.empty());
        }
        return context;
    }

    private Communication prepareCommunication(PlatformRecord platformRecord, Optional<Integer> workerID) {
        Communication communication = mock(Communication.class);
        when(communication.tryGetWorkerID(platformRecord.getIdPlatform(), null))
                .thenReturn(CompletableFuture.completedFuture(workerID));
        return communication;
    }
}