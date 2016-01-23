package edu.kit.ipd.crowdcontrol.workerservice.query;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.BadRequestException;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.database.OperationsHelper;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.TaskStopgap;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PopulationAnswerOptionRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PopulationRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.jooq.Result;
import org.junit.Test;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class QueryTest {
    private final JsonFormat.Parser parser = JsonFormat.parser();
    private final OperationsHelper operationsHelper = new OperationsHelper();


    @Test
    public void testExpectEmail() throws Exception {
        int experimentID = 1;
        String platform = "a";
        Request request = prepareRequest(experimentID, platform, -1);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, true, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID);
        Query query =  new Query(null, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.EMAIL));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testExpectEmailFail() throws Exception {
        int experimentID = 1;
        String platform = "a";
        Request request = prepareRequest(experimentID, platform, -1);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID);
        Query query =  new Query(null, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.EMAIL));
    }

    @Test
    public void testExpectGetWorkerID() throws Exception {
        int experimentID = 1;
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentID, platform, workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.of(workerID));
        PopulationsOperations populationsOperations = operationsHelper.preparePopulationOperations(experimentID, platform, workerID, true, null);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testProvideWorkerID() throws Exception {
        int experimentID = 1;
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentID, platform, workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations = operationsHelper.preparePopulationOperations(experimentID, platform, workerID, true, null);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test(expected= BadRequestException.class)
    public void testProvideStringWorkerID() throws Exception {
        int experimentID = 1;
        String platform = "a";
        String workerID = "af";
        Request request = prepareRequest(experimentID, platform, -1);
        when(request.queryParams("worker")).thenReturn(workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations = operationsHelper.preparePopulationOperations(experimentID, platform, 3, true, null);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testCalibrations() throws Exception {
        int experimentID = 1;
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentID, platform, workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        Map<PopulationRecord, Result<PopulationAnswerOptionRecord>> calibrations = operationsHelper.generatePopulations(experimentID);
        PopulationsOperations populationsOperations = operationsHelper.preparePopulationOperations(experimentID, platform, workerID, false, calibrations);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.CALIBRATION));
        assertTrue(builder.getCalibrationsList().size() == calibrations.size());
        Optional<Map.Entry<PopulationRecord, Result<PopulationAnswerOptionRecord>>> res = calibrations.entrySet().stream()
                .filter(entry -> !builder.getCalibrationsList().stream()
                        .filter(calibration -> calibration.getQuestion().equals(entry.getKey().getProperty()))
                        .filter(calibration -> calibration.getId() == entry.getKey().getIdPopulation())
                        .flatMap(calibration -> calibration.getAnswerOptionsList().stream())
                        .filter(answerOption ->
                                entry.getValue().stream().anyMatch(record ->
                                        record.getAnswer().equals(answerOption.getOption())))
                        .anyMatch(answerOption ->
                                entry.getValue().stream().anyMatch(record ->
                                        record.getIdPopulationAnswerOption().equals(answerOption.getId())))
                ).findAny();
        assertTrue(!res.isPresent());
    }

    @Test
    public void testSkipAll() throws Exception {
        int experimentID = 1;
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentID, platform, workerID);
        when(request.queryParams("answer")).thenReturn("skip");
        when(request.queryParams("rating")).thenReturn("skip");
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentID, platform, workerID, false, new HashMap<>());
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testCreative() throws Exception {
        ExperimentRecord experimentRecord = operationsHelper.generateExperimentRecord();
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentRecord.getIdExperiment(), platform, workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentRecord.getIdExperiment(), platform, workerID, false, new HashMap<>());
        String mockTaskChooserDescription = "mockTaskChooserDescription";
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentRecord, mockTaskChooserDescription, new ArrayList<>(), null);
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experimentRecord, workerID, 0, 0, 0, new ArrayList<>(), platform, null);
        MockTaskChooser mockTaskChooser = new MockTaskChooser(false, true, experimentRecord, experimentOperations, taskOperations, 10, 10);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, taskOperations, mockTaskChooser);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.ANSWER));
    }

    @Test
    public void testRating() throws Exception {
        ExperimentRecord experimentRecord = operationsHelper.generateExperimentRecord();
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentRecord.getIdExperiment(), platform, workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentRecord.getIdExperiment(), platform, workerID, false, new HashMap<>());
        String mockTaskChooserDescription = "mockTaskChooserDescription";
        int ratingAmount = (int) (100 * (Math.random()));
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentRecord, mockTaskChooserDescription, new ArrayList<>(), null);
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experimentRecord, workerID, 100, 10, 10, new ArrayList<>(), platform, null);
        MockTaskChooser mockTaskChooser = new MockTaskChooser(experimentRecord.getAlgorithmTaskChooser(), mockTaskChooserDescription, false, false, experimentOperations, taskOperations, 0, ratingAmount);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, taskOperations, mockTaskChooser);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.RATING));
    }

    @Test
    public void testStopgapAnswer() throws Exception {
        ExperimentRecord experimentRecord = operationsHelper.generateExperimentRecord();
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentRecord.getIdExperiment(), platform, workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentRecord.getIdExperiment(), platform, workerID, false, new HashMap<>());
        String mockTaskChooserDescription = "mockTaskChooserDescription";
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentRecord, mockTaskChooserDescription, new ArrayList<>(), null);
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experimentRecord, workerID, 0, 0, 0, new ArrayList<>(), platform, TaskStopgap.answer);
        MockTaskChooser mockTaskChooser = new MockTaskChooser(false, false, experimentRecord, experimentOperations, taskOperations, 10, 10);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, taskOperations, mockTaskChooser);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testStopgapAnswerRating() throws Exception {
        ExperimentRecord experimentRecord = operationsHelper.generateExperimentRecord();
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentRecord.getIdExperiment(), platform, workerID);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentRecord.getIdExperiment(), platform, workerID, false, new HashMap<>());
        String mockTaskChooserDescription = "mockTaskChooserDescription";
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentRecord, mockTaskChooserDescription, new ArrayList<>(), null);
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experimentRecord, workerID, 0, 0, 0, new ArrayList<>(), platform, TaskStopgap.rating);
        MockTaskChooser mockTaskChooser = new MockTaskChooser(false, true, experimentRecord, experimentOperations, taskOperations, 10, 10);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, taskOperations, mockTaskChooser);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.RATING));
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
        return mock;
    }

    private Communication prepareCommunication(String platform, Optional<Integer> workerID) {
        Communication communication = mock(Communication.class);
        when(communication.tryGetWorkerID(platform, null))
                .thenReturn(CompletableFuture.completedFuture(workerID));
        return communication;
    }
}