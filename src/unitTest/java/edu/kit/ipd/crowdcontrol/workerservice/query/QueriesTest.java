package edu.kit.ipd.crowdcontrol.workerservice.query;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.TaskStopgap;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.CalibrationAnswerOptionRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.CalibrationRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PlatformRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.*;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.jooq.Result;
import org.junit.Before;
import org.junit.Test;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class QueriesTest {
    private final JsonFormat.Parser parser = JsonFormat.parser();

    private OperationsDataHolder data;

    @Before
    public void setUp() {
        data = new OperationsDataHolder();
    }

    @Test
    public void testExpectEmail() throws Exception {
        Request request = prepareRequest(data.getExperimentRecord().getIdExperiment(), data.getPlatformRecord().getIdPlatform(), -1);
        Response response = mock(Response.class);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.EMAIL));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testExpectEmailFail() throws Exception {
        data.getPlatformRecord().setNeedsEmail(false);
        Request request = prepareRequest(data.getExperimentRecord().getIdExperiment(), data.getPlatformRecord().getIdPlatform(), -1);
        Response response = mock(Response.class);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        queries.getNext(request, response);
    }

    @Test
    public void testExpectGetWorkerID() throws Exception {
        data.setBelongsToWrongPopulation(true);
        data.getPlatformRecord().setNeedsEmail(false);
        Request request = prepareRequest(data.getExperimentRecord().getIdExperiment(), data.getPlatformRecord().getIdPlatform(), -1);
        Response response = mock(Response.class);
        Queries queries =  prepareQuery(data, Optional.of(data.getWorkerID()), null);
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getWorkerId() == data.getWorkerID());
    }


    @Test
    public void testProvideWorkerID() throws Exception {
        Request request = prepareRequest(data);
        Response response = mock(Response.class);
        Queries queries = prepareQuery(data, Optional.empty(), null);
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.CALIBRATION));
    }

    @Test(expected= BadRequestException.class)
    public void testProvideStringWorkerID() throws Exception {
        String workerID = "af";
        Request request = prepareRequest(data);
        when(request.queryParams("worker")).thenReturn(workerID);
        Response response = mock(Response.class);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        queries.getNext(request, response);
    }


    @Test
    public void testCalibrations() throws Exception {
        Request request = prepareRequest(data);
        Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> calibrations = data.getCalibrations();
        Response response = mock(Response.class);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.CALIBRATION));
        assertTrue(builder.getCalibrationsList().size() == calibrations.size());
        Optional<Map.Entry<CalibrationRecord, Result<CalibrationAnswerOptionRecord>>> res = calibrations.entrySet().stream()
                .filter(entry -> !builder.getCalibrationsList().stream()
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
        Request request = prepareRequest(data);
        Response response = mock(Response.class);
        Queries queries = prepareQuery(data, Optional.empty(), null);
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testInvalidQuality() throws Exception {
        data.setWorkerQuality(data.getExperimentRecord().getWorkerQualityThreshold() - 1);
        Request request = prepareRequest(data);
        Response response = mock(Response.class);
        Queries queries = prepareQuery(data, Optional.empty(), null);
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testSkipAll() throws Exception {
        data.setCalibrations(new HashMap<>());
        Request request = prepareRequest(data);
        when(request.queryParams("answer")).thenReturn("skip");
        when(request.queryParams("rating")).thenReturn("skip");
        Response response = mock(Response.class);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
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
        data.getTaskRecord().setStopgap(TaskStopgap.answer);
        View.Builder builder = testWithTaskChooser(false, false);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testStopgapAnswerRating() throws Exception {
        data.getTaskRecord().setStopgap(TaskStopgap.rating);
        View.Builder builder = testWithTaskChooser(false, true);
        assertTrue(builder.getType().equals(View.Type.RATING));
    }

    @Test
    public void testPreview() throws Exception {
        Request request = prepareRequest(data);
        Response response = mock(Response.class);
        Queries queries =  prepareQuery(data, Optional.empty(), null);
        String json = queries.preview(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
        assertTrue(builder.getTitle().equals(data.getExperimentRecord().getTitle()));
        assertTrue(builder.getDescription().equals(data.getExperimentRecord().getDescription()));
        assertTrue(builder.getConstraintsList().size() == data.getConstraints().size());
    }

    private View.Builder testWithTaskChooser(boolean finish, boolean creative) throws Exception {
        data.setCalibrations(new HashMap<>());
        Request request = prepareRequest(data);
        Response response = mock(Response.class);
        String mockTaskChooserDescription = "mockTaskChooserDescription";
        data.getExperimentRecord().setAlgorithmTaskChooser(mockTaskChooserDescription);
        ExperimentOperations experimentOperations = data.createExperimentOperations();
        TaskOperations taskOperations = data.createTaskOperations();
        CalibrationsOperations calibrationsOperations = data.createPopulationsOperations();
        PlatformOperations platformOperations = data.createPlatformOperations();
        Communication communication = prepareCommunication(data.getPlatformRecord(), Optional.empty());
        MockTaskChooser mockTaskChooser = new MockTaskChooser(finish, creative, data.getExperimentRecord(), experimentOperations, taskOperations);
        Queries queries =  new Queries(calibrationsOperations, experimentOperations, platformOperations, communication, taskOperations, mockTaskChooser, data.createWorkerOperations());
        String json = queries.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        return builder;
    }

    private Queries prepareQuery(OperationsDataHolder data, Optional<Integer> communicationReturn, TaskChooserAlgorithm taskChooserAlgorithm) {
        Communication communication = prepareCommunication(data.getPlatformRecord(), communicationReturn);
        return new Queries(data.createPopulationsOperations(), data.createExperimentOperations(), data.createPlatformOperations(),
                communication, data.createTaskOperations(), taskChooserAlgorithm, data.createWorkerOperations());
    }

    private Request prepareRequest(OperationsDataHolder dataHolder) {
        return prepareRequest(dataHolder.getExperimentRecord().getIdExperiment(), dataHolder.getPlatformRecord().getIdPlatform(), dataHolder.getWorkerID());
    }

    private Request prepareRequest(int experimentID, String platform, int workerID) {
        Request mock = mock(Request.class);
        when(mock.params("platform")).thenReturn(platform);
        when(mock.params("experiment")).thenReturn(String.valueOf(experimentID));
        if (workerID != -1)
            when(mock.queryParams("worker")).thenReturn(String.valueOf(workerID));
        QueryParamsMap queryParamsMap = mock(QueryParamsMap.class);
        when(queryParamsMap.toMap()).thenReturn(null);
        when(mock.queryMap()).thenReturn(queryParamsMap);
        when(mock.headers("accept")).thenReturn("application/json");
        return mock;
    }

    private Communication prepareCommunication(PlatformRecord platformRecord, Optional<Integer> workerID) {
        Communication communication = mock(Communication.class);
        when(communication.tryGetWorkerID(platformRecord.getIdPlatform(), null))
                .thenReturn(CompletableFuture.completedFuture(workerID));
        return communication;
    }
}