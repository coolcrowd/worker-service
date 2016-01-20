package edu.kit.ipd.crowdcontrol.workerservice.query;

import com.google.protobuf.util.JsonFormat;
import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import edu.kit.ipd.crowdcontrol.workerservice.database.OperationsHelper;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PopulationRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PopulationAnswerOptionRecord;
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

import java.util.*;
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
        Request request = prepareRequest(experimentID, platform, null);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, true, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, null, null, null);
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
        Request request = prepareRequest(experimentID, platform, null);
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, null, null, null);
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
        Request request = prepareRequest(experimentID, platform, null);
        //when(request.queryParams("worker")).thenReturn(String.valueOf(workerID));
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.of(workerID));
        PopulationsOperations populationsOperations = operationsHelper.preparePopulationOperations(experimentID, platform, workerID, true, null);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, null, null, null);
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
        Request request = prepareRequest(experimentID, platform, null);
        when(request.queryParams("worker")).thenReturn(String.valueOf(workerID));
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations = operationsHelper.preparePopulationOperations(experimentID, platform, workerID, true, null);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, null, null, null);
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
        Request request = prepareRequest(experimentID, platform, null);
        when(request.queryParams("worker")).thenReturn(String.valueOf(workerID));
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        Map<PopulationRecord, Result<PopulationAnswerOptionRecord>> calibrations = operationsHelper.generatePopulations(experimentID);
        PopulationsOperations populationsOperations = operationsHelper.preparePopulationOperations(experimentID, platform, workerID, false, calibrations);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, null, null, null);
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
        Request request = prepareRequest(experimentID, platform, null);
        when(request.queryParams("worker")).thenReturn(String.valueOf(workerID));
        when(request.queryParams("answer")).thenReturn("skip");
        when(request.queryParams("rating")).thenReturn("skip");
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentID, platform, workerID, false, new HashMap<>());
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, null, null, null);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.FINISHED));
    }

    @Test
    public void testCreative() throws Exception {
        int experimentID = 1;
        int task = 2;
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentID, platform, null);
        when(request.queryParams("worker")).thenReturn(String.valueOf(workerID));
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentID, platform, workerID, false, new HashMap<>());
        String mockTaskChooserName = "mockTaskChooser";
        String description = "description";
        String pictureUrl = "ww.xy.de";
        String pictureLUrl = "ww.xyz.de";
        String picture = "{!" + pictureUrl + " " + pictureLUrl + "}";
        ExperimentRecord experimentRecord = operationsHelper.prepareExperimentRecord(experimentID, 3, mockTaskChooserName, "title", description+picture);
        List<String> constraints = Arrays.asList("const1", "const2");
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, experimentRecord, mockTaskChooserName, constraints);
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experimentID, platform, workerID, 0, new ArrayList<>());
        MockTaskChooser mockTaskChooser = new MockTaskChooser(mockTaskChooserName, false, true, experimentOperations, taskOperations);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null, mockTaskChooser);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.ANSWER));
    }

    @Test
    public void testRating() throws Exception {
        int experimentID = 1;
        int task = 2;
        String platform = "a";
        int workerID = 3;
        Request request = prepareRequest(experimentID, platform, null);
        when(request.queryParams("worker")).thenReturn(String.valueOf(workerID));
        Response response = mock(Response.class);
        PlatformOperations platformOperations = operationsHelper.preparePlatformOperations(platform, false, true);
        Communication communication = prepareCommunication(platform, Optional.empty());
        PopulationsOperations populationsOperations =
                operationsHelper.preparePopulationOperations(experimentID, platform, workerID, false, new HashMap<>());
        String mockTaskChooserName = "mockTaskChooser";
        ExperimentRecord experimentRecord = operationsHelper.prepareExperimentRecord(experimentID, 3, mockTaskChooserName, "title", "description");
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, experimentRecord, mockTaskChooserName, new ArrayList<>());
        List<AnswerRecord> answerRecords = operationsHelper.generateAnswers(experimentRecord.getRatingsPerAnswer(), experimentID);
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experimentID, platform, workerID, experimentRecord.getRatingsPerAnswer(), answerRecords);
        MockTaskChooser mockTaskChooser = new MockTaskChooser(mockTaskChooserName, false, false, experimentOperations, taskOperations);
        Query query =  new Query(populationsOperations, experimentOperations, platformOperations, communication, null, mockTaskChooser);
        String json = query.getNext(request, response);
        View.Builder builder = View.newBuilder();
        parser.merge(json, builder);
        assertTrue(builder.getType().equals(View.Type.RATING));
    }

    private Request prepareRequest(int experimentID, String platform, Map<String, String[]> queryMap) {
        Request mock = mock(Request.class);
        when(mock.params("platform")).thenReturn(platform);
        when(mock.params("experiment")).thenReturn(String.valueOf(experimentID));
        QueryParamsMap queryParamsMap = mock(QueryParamsMap.class);
        when(queryParamsMap.toMap()).thenReturn(queryMap);
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